package com.relationaldata.organisationdata;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public class ValidateCertificate
{
    public static void main(String[] args)
    {
        try
        {
            // this is using factory design pattern

            // This class defines the functionality of a certificate factory, which is used to generate certificate,
            // certification path (CertPath)
            // and certificate revocation list (CRL) objects from their encodings.
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            // A certificate factory for X.509 must return certificates that are an instance of java.security.cert.X509Certificate
            // Abstract class for X.509 certificates. This provides a standard way to access all the attributes of an X.509 certificate
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(new File(args[0])));

            byte[] crlDistributionPointDerEncodedArray = certificate.getExtensionValue(Extension.cRLDistributionPoints.getId());

            // Create an ASN1InputStream based on the input byte array. array containing ASN.1 encoded data.
            ASN1InputStream oAsnInStream = new ASN1InputStream(new ByteArrayInputStream(crlDistributionPointDerEncodedArray));

            // Create a base ASN.1 object from a byte stream.
            ASN1Primitive derObjCrlDP = oAsnInStream.readObject();  // The method readObject is used to read an object from the stream.

            // the octets making up the octet string.
            DEROctetString dosCrlDP = (DEROctetString) derObjCrlDP;

            oAsnInStream.close();

            // Return the content of the OCTET STRING as a byte array.
            byte[] crldpExtOctets = dosCrlDP.getOctets();

            ASN1InputStream oAsnInStream2 = new ASN1InputStream(new ByteArrayInputStream(crldpExtOctets));
            ASN1Primitive derObj2 = oAsnInStream2.readObject();
            CRLDistPoint distPoint = CRLDistPoint.getInstance(derObj2);

            oAsnInStream2.close();

            List<String> crlUrls = new ArrayList<>();

            // Return the distribution points making up the sequence.
            for (DistributionPoint dp : distPoint.getDistributionPoints())
            {
                DistributionPointName dpn = dp.getDistributionPoint();
                // Look for URIs in fullName
                if (dpn != null)
                {
                    if (dpn.getType() == DistributionPointName.FULL_NAME)
                    {
                        GeneralName[] genNames = GeneralNames.getInstance(dpn.getName()).getNames();
                        // Look for an URI
                        for (GeneralName genName : genNames) {
                            if (genName.getTagNo() == GeneralName.uniformResourceIdentifier) {
                                String url = DERIA5String.getInstance(genName.getName()).getString();
                                crlUrls.add(url);
                            }
                        }
                    }
                }
            }

            X509CRLEntry revokedCertificate = null;
            X509CRL crl = null;

            Date expiryDate = certificate.getNotAfter();
            Date currentDate = java.util.Calendar.getInstance().getTime();

            for (String url : crlUrls) {
                URL urlCon = new URL(url);
                // making connection with url for fetching crlData
                URLConnection connection = urlCon.openConnection();

                // fetching crl data from the url found from certificate
                try(DataInputStream inStream = new DataInputStream(connection.getInputStream())){
                    // generating CRL using Certificatefatcory property
                    crl = (X509CRL)certificateFactory.generateCRL(inStream);
                }
                // verify certificate serial number in crl if certificacte is revoked
                revokedCertificate = crl.getRevokedCertificate(certificate.getSerialNumber());

                if(revokedCertificate !=null){
                    System.out.println("This certificate is Revoked on "+revokedCertificate.getRevocationDate());
                }
                else if(currentDate.after(expiryDate)){
                    System.out.println("This certificate is expired on "+expiryDate);
                }
                else{
                    System.out.println("This certificate is Valid till " +expiryDate);
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
