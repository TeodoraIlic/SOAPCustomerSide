package com.teodora.ws.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;


import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.example.customerinformation.CustomerInformationInterface;
import org.example.customerinformation.GetCustomerDetails;
import org.example.customerinformation.GetCustomerDetails1;
import org.example.customerinformation.GetCustomerDetailsFaultException;
import org.example.customerinformation.GetCustomerDetailsResponse1;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.teodora.ws.soap.entities.Customer;

@SpringBootApplication
public class MainApp {

	public static void main(String[] args) throws IOException, URISyntaxException, TransformerException  {

		 String firstName;
		 String lastName;
		 String mail;
		 String phone;
		 String age;
		 String status;
		 String address;
		 String city;
		 
		try {
			
			CustomerImplService service = new CustomerImplService(
					new URL("http://localhost:8080/hellojobhomework/customerordersservice?wsdl"));
			
			CustomerInformationInterface customerImplPort = service.getCustomerImplPort();

			GetCustomerDetails1 customer = new GetCustomerDetails1();
			customer.setId("1");
			GetCustomerDetails customer1 = new GetCustomerDetails();
			customer1.setGetCustomerDetails(customer);

			try {

				GetCustomerDetailsResponse1 response = customerImplPort.getCustomerDetails(customer);

				System.out.println(response.getFirstName().toString());

				firstName = response.getFirstName().toString();
				lastName = response.getLastName();
				mail = response.getContactEmail();
				phone = response.getContactPhone();
				age = response.getAge();
				status = response.getMartialStatus();
				address = response.getAddress();
				city = response.getCity();

				Customer1 custResponse = new Customer1();
				
				custResponse.setAddress(address);
				custResponse.setAge(age);
				custResponse.setCity(city);
				custResponse.setContactEmail(mail);
				custResponse.setContactPhone(phone);
				custResponse.setFirstName(firstName);
				custResponse.setLastName(lastName);
				custResponse.setMartialStatus(status);
				
				ResponseToXml(custResponse);
					

				// upisujemo u bazu
				Customer cust = new Customer();
				cust.setId("1");
				cust.setFirstName(firstName);
				cust.setLastName(lastName);
				cust.setMail(mail);
				cust.setAddress(address);
				cust.setAge(age);
				cust.setPhone(phone);
				cust.setStatus(status);
				cust.setCisty(city);

					
					
				insertCustomer(cust);
					
					
				//transformacija
				TransformerFactory factory = TransformerFactory.newInstance();
		        Source xslt = new StreamSource(new File("C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/transform.xsl"));
		        Transformer transformer = factory.newTransformer(xslt);

		        Source text = new StreamSource(new File("C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/customer1.xml"));
		        transformer.transform(text, new StreamResult(new File("C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/customer2.xml")));
		    
				
			} catch (GetCustomerDetailsFaultException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		

	}
	
	public static void ResponseToXml(Customer1 response) {
	
		try {
		JAXBContext context = JAXBContext.newInstance(Customer1.class);
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		
		//Schema schema = sf.newSchema(new File("C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/Customer1.xsd"));
		
		Marshaller createMarshaller = context.createMarshaller();
		createMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//createMarshaller.setSchema(schema);
		createMarshaller.marshal(response, System.out);
		
		
		OutputStream os = new FileOutputStream(
				"C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/customer1.xml");
		
		
		createMarshaller.marshal(response, os);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		/*try {
			//JAXBContext jc = JAXBContext.newInstance(Customer1.class);
			//JAXBSource source = new JAXBSource(jc, response);

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		
			Schema schema = sf.newSchema(new File("C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/src/main/xsd/Customer1.xsd")); 
		
			File file = new File(
					"C:/Users/Teodora/Documents/workspace-sts-3.9.6.RELEASE/hellowebserviceClient/customer1.xml");
			
			Validator validator = schema.newValidator();
			SAXSource source = new SAXSource(
	                new NamespaceFilter(XMLReaderFactory.createXMLReader()),
	                new InputSource(new FileInputStream(file)));

			validator.setErrorHandler(new MyErrorHandler());
			validator.validate(source);
			 System.out.println(source.getSystemId() + " is valid");
		} catch (SAXException e) {
			System.out.println( "File is NOT valid reason:" + e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	protected static class NamespaceFilter extends XMLFilterImpl {

	    String requiredNamespace = "http://www.example.org/Customer1";

	    public NamespaceFilter(XMLReader parent) {
	        super(parent);
	    }
	    @Override
	    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
	        if(!arg0.equals(requiredNamespace)) 
	            arg0 = requiredNamespace;
	        super.startElement(arg0, arg1, arg2, arg3);
	    }  
	         
	}
	
	public static void insertCustomer(Customer customer){
		
		/*Otvaranje sesije*/
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		
		/* Pocetak transakcije */		
		Transaction TR = session.beginTransaction();
		try {
			/* Unosi se kreirani smer u tabelu smer u bazi */
			
			session.save(customer); 
			System.out.println("Upisan je jedan klijent!");
			
			/* Potvrdjivanje i zavrsavanje transakcije */
			TR.commit();
			/*Zatvaranje sesije*/
		} catch (Exception e) {
			
			System.err.println("Cuvanje klijenta nije uspelo! Ponistavanje transakcije!");
			
			/* Ponistavanje transakcije */
			TR.rollback();
		}finally{
			session.close();
		}
	}


}
