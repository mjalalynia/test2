/**
 * Created by ${Dotin} on ${4/25/2015}.
 */

import java.io.*;

import java.math.BigDecimal;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlReader {
    private enum depositType {ShortTerm, Qarz, LongTerm}

    private static boolean flag = false;

    private static TreeSet parse()  {
        TreeSet<Deposit> depositTreeSet = new TreeSet<>();
        String nullMsg = " the value is null!";
        String wrongMsg = "deposit type is wrong!";
        //extract feature from xml file
        File file = new File("deposits.xml");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            if (builder != null) {
                doc = builder.parse(file);
            }
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        NodeList nodeLst = null;
        if (doc != null) {
            doc.getDocumentElement().normalize();
            nodeLst = doc.getElementsByTagName("deposit");
        }

        String customerNumber;
        if (nodeLst != null)
            for (int counter = 0; counter < nodeLst.getLength(); counter++) {
                BigDecimal depositBalance;
                int durationDays;

                Node fstNode = nodeLst.item(counter);
                String depositTypeName = null;
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element fstElmnt = (Element) fstNode;
                    NodeList customerNumberElmntLst = fstElmnt.getElementsByTagName("customerNumber");
                    Element customerNumberElmnt = (Element) customerNumberElmntLst.item(0);
                    NodeList customerNum = customerNumberElmnt.getChildNodes();
                    customerNumber = customerNum.item(0).getNodeValue();
                    try {
                        NodeList depositTypeElmntLst = fstElmnt.getElementsByTagName("depositType");
                        Element depositTypeElmnt = (Element) depositTypeElmntLst.item(0);
                        NodeList depositTypeNode = depositTypeElmnt.getChildNodes();
                        if (depositTypeNode.item(0) != null)
                            depositTypeName = depositTypeNode.item(0).getNodeValue();
                        //DepositTypeException typeException = new DepositTypeException();
                        if (!isInEnum(depositTypeName, depositType.class)) {
                            throw new DepositTypeException(wrongMsg);
                        }
                    }catch (DepositTypeException e)
                    {
                        logError(counter, e.toString());
                        continue;
                    }

                    try {
                        NodeList depositBalanceElmntLst = fstElmnt.getElementsByTagName("depositBalance");
                        Element depositBalanceElmnt = (Element) depositBalanceElmntLst.item(0);
                        NodeList depositBalanceNode = depositBalanceElmnt.getChildNodes();
                        if (depositBalanceNode.item(0) != null)
                            depositBalance = new BigDecimal(depositBalanceNode.item(0).getNodeValue());
                        else {
                            logError(counter, nullMsg);
                            continue;
                        }
                        if(depositBalance.doubleValue() <0)
                            throw  new NegativeValueExeption("the deposit balance is negative!!");
//                        MyNumberFormatException formatException = new MyNumberFormatException();
//                        formatException.main(depositBalance);
                    } catch (NegativeValueExeption e) {
                        logError(counter, e.toString());
                        continue;
                    }
                    try {
                        NodeList durationDaysElmntLst = fstElmnt.getElementsByTagName("durationlnDays");
                        Element durationDaysElmnt = (Element) durationDaysElmntLst.item(0);
                        NodeList durationDaysNode = durationDaysElmnt.getChildNodes();
                        if (durationDaysNode.item(0) != null)
                            durationDays = Integer.valueOf(durationDaysNode.item(0).getNodeValue());
                        else {
                            logError(counter, nullMsg);
                            continue;
                        }
//                        MyNumberFormatException formatException = new MyNumberFormatException();
//                        formatException.badFormatDays(durationDays);
                    } catch (NumberFormatException e) {
                        logError(counter, e.toString());
                        continue;
                    }
                    Deposit deposit;
                    Class depositTypeClass;
                    try {
                        depositTypeClass = Class.forName(depositTypeName);
                    } catch (ClassNotFoundException e) {
                        //e.printStackTrace();
                        //depositTypeClass = null;
                        logError(counter,e.toString());
                        continue;
                    }
                    Object depositTypeObj = null;
                    try {
                        if (depositTypeClass != null)
                            depositTypeObj = depositTypeClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (depositTypeObj != null) {
                        try {
                            deposit = new Deposit(customerNumber, depositBalance, durationDays, (DepositType) depositTypeObj);
                        }catch (NegativeValueExeption e)
                        {
                            logError(counter,e.toString());
                            continue;
                        }
                        deposit.depositInterestCal();
                        depositTreeSet.add(deposit);
                    }
                }
            }

        return depositTreeSet;
    }

    private static void logError(int c, String msg) {
        String errorMassage;
        errorMassage = "the " + (c + 1) + "th input is corrupted!! " + msg + "\n";
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", flag)))) {
            out.println(errorMassage);
        } catch (IOException e) {
            logError(0,e.toString());
        }
        flag = true;
    }

    private static void writeResult() {
        TreeSet deposit = parse();
        String content = "";
        Iterator it = deposit.iterator();

        while (it.hasNext()) {
            Deposit next = (Deposit) it.next();
            content = content + (" customerNumber: " + next.customerNumber + " depositInterest: " + next.depositInterest + "\n");
        }
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output.txt", flag)))) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
    public static void main(String argv[]) {
        writeResult();
    }
}
//        FileOutputStream fop ;
//        File file;
//        try {
//            file = new File("output.txt");
//            fop = new FileOutputStream(file);
//            if (!file.exists()) file.createNewFile();
//            byte[] contentInBytes = errorMassage.getBytes();
//            fop.write(contentInBytes);
//            fop.flush();
//            fop.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

