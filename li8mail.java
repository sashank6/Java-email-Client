import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.security.Security;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

public class li8mail extends JFrame implements ItemListener,ActionListener,FocusListener
{
    JLabel l1=new JLabel("From:");JLabel[] at=new JLabel[20];
    JTextField t1=new JTextField();
    JTextField t2,t3,t4;JLabel l2,l5,l3,l4,l6;
    JPasswordField pf;JComboBox cb;JTextArea ta;JButton b1,b2,b3;
    JScrollPane jsp;int i=0;String server;int port=587;int sinx=0;
    boolean flagfrom,flagpwd,flagto;JButton b4=new JButton("Attach Files");int ty=1;File f;
    String[] fpath=new String[10];JLabel hut=new JLabel("");
    li8mail()
    {
        setLayout(null);
        l1.setBounds(30,10, 100,20);
        add(l1);
        t1.setBounds(100,10, 500, 20);
        add(t1);
        t1.addFocusListener(this);
        l2=new JLabel("Password:");
        l2.setBounds(30, 40, 70, 20);add(l2);
        pf=new JPasswordField();
        pf.setBounds(100, 40, 500, 20);
        add(pf);
        pf.addFocusListener(this);
        l3=new JLabel("ESP:");
        l3.setBounds(30,70,50,20);
        add(l3);
        cb=new JComboBox();
        cb.addItem("Gmail");
        cb.addItem("live ID");
        cb.addItem("Yahoo");
        cb.setBounds(100, 70, 80, 20);
        add(cb);
        l4=new JLabel("To:");
        l4.setBounds(30,100,50,20);
        add(l4);
        t2=new JTextField();
        t2.setBounds(100,100,500,20);
        add(t2);
        t2.addFocusListener(this);
        l5=new JLabel("Subject:");
        l5.setBounds(30,130,50,20);
        add(l5);
        t3=new JTextField();
        t3.setBounds(100, 130, 500, 20);
        add(t3);
        at[0]=new JLabel("Attachments:");
        at[0].setBounds(30, 160, 90, 20);
        add(at[0]);
        b4.setBounds(120, 160, 130, 20);
        add(b4);
        b4.addActionListener(this);
        int x=100;int y=190;
        for(int t=1;t<7;t++)
        {
            at[t]=new JLabel("");
            at[t].setBounds(x, y, 80,20);
            x+=90;
            add(at[t]);
        }

        l6=new JLabel("Message:");
        l6.setBounds(30, 220, 70, 20);
        add(l6);
        ta=new JTextArea();
        JScrollPane areaScrollPane = new JScrollPane(ta);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(300,300));
        areaScrollPane.setBounds(100, 220, 500, 300);
        add(areaScrollPane);
        ta.setFont(new Font("Calibri", Font.PLAIN, 14));
        cb.addItemListener(this);

        b1=new JButton("Send");
        b1.setBounds(100, 540, 80, 30);
        add(b1);
        b1.addActionListener(this);
        b2=new JButton("Reset");
        b2.setBounds(200, 540, 80, 30);
        add(b2);
        b2.addActionListener(this);
        b3=new JButton("Close");
        b3.setBounds(300, 540, 80, 30);
        add(b3);
        b3.addActionListener(this);
        hut.setBounds(100,590,200,20);
        add(hut);

    }
    public static void main(String[] args)
    {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        li8mail obj=new li8mail();
        obj.setTitle("li8Mail 1.0");
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.setVisible(true);
        obj.setSize(640, 720);

    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {

        i=cb.getSelectedIndex();

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==b1)
        {
            hut.setText("Sending...");
            try
            {
                boolean status=send1();
            if(status && sinx!=3 && sinx!=5)
            {
                hut.setText("Sent");
                JOptionPane.showMessageDialog(rootPane, "Mail Delivered");
            }
            else
                if(!status && sinx!=3&& sinx!=5)
                JOptionPane.showMessageDialog(rootPane,"Mail Delivery Failed");

            }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(rootPane,"Mail Delivery Failed");
        }
        }
        if(e.getSource()==b3)
        {
            System.exit(0);
        }
        if(e.getSource()==b2)
        {
            t1.setText("");
            t2.setText("");
            t3.setText("");
            pf.setText("");
            ta.setText("");
        }
        if(e.getSource()==b4)
        {
            if(ty>=7)
                JOptionPane.showMessageDialog(rootPane, "File Attachments Exceeded");
            else
            {
            JFileChooser jfc=new JFileChooser();
            int q=jfc.showOpenDialog(this);
            if(q==JFileChooser.APPROVE_OPTION)
                f=jfc.getSelectedFile();
            if(f.length()>10485760)
                JOptionPane.showMessageDialog(rootPane, "File Size Exceded");
            else
            {

            fpath[ty]=f.getPath();
            at[ty].setText(f.getName());
            ty++;
            }
            }
        }
    }
    boolean send1() throws AddressException, MessagingException
    {
        hut.setText("Sending...");
        final String id=t1.getText();
        char[] c=pf.getPassword();
        int len=c.length;
        String pp="";
        for(int k=0;k<len;k++)
            pp+=String.valueOf(c[k]);
        final String pwd=pp;
        switch(i)
        {
            case 0:
                server="smtp.gmail.com";
                break;
            case 1:
                server="smtp.live.com";
                break;
            case 2:
                server="smtp.mail.yahoo.com";
                break;
        }
        String sub=t3.getText();
        String to=t2.getText();
        String msg=ta.getText();
        Properties prop=new Properties();
        prop.put("mail.smtp.host",server);
        prop.put("mail.smtp.auth",true);
        prop.put("mail.smtp.port",port);
        prop.put("mail.debug",true);
        String[] to2=to.split("\\ ");
        int len1=to2.length;
        int ch=-99;int bch=-99;
        if(sub.equalsIgnoreCase("")|| sub.equalsIgnoreCase(" "))
            ch=JOptionPane.showConfirmDialog(rootPane, "Do you want to Send Mail without any Subject?");
        if(ch==1)
        {
            t3.requestFocus();
            sinx=3;
        }

        if(sinx==3)
            return false;
        Session ses=Session.getDefaultInstance(prop,new Authenticator()
        {
             protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(id,pwd);
            }

        });
        Multipart mp=new MimeMultipart();
        MimeBodyPart[] mbp=new MimeBodyPart[8];
        mbp[0]=new MimeBodyPart();
        mbp[0].setText(msg);
        mp.addBodyPart(mbp[0]);
        for(int u=1;u<ty;u++)
        {
            DataSource ds=new FileDataSource(fpath[u]);
            mbp[u]=new MimeBodyPart();
            mbp[u].setDataHandler(new DataHandler(ds));
            mbp[u].setFileName(ds.getName());
            mp.addBodyPart(mbp[u]);
        }
        ses.setDebug(false);
        InternetAddress from1=new InternetAddress(id);
        InternetAddress[] to1=new InternetAddress[len1];
        Message masg=new MimeMessage(ses);
        masg.setSubject(sub);
        masg.setFrom(from1);
        masg.setContent(mp);
        for(int y=0;y<len1;y++)
            to1[y]=new InternetAddress(to2[y]);
        masg.setRecipients(Message.RecipientType.TO, to1);
        try
        {
            Transport.send(masg);
        }
        catch(Exception er)
        {
            er.printStackTrace();
            return false;
        }

        return true;
    }
    @Override
    public void focusGained(FocusEvent e)
    {
        if(e.getSource()==t1)
            flagfrom=false;
        if(e.getSource()==t2)
        {
            flagto=false;

        }
    }
    @Override
    public void focusLost(FocusEvent e)
    {
        if(e.getSource()==t1 && !flagfrom)
        {
            if(t1.getText().equalsIgnoreCase("")||t1.getText().equalsIgnoreCase(" "))
            {
                JOptionPane.showMessageDialog(rootPane, "From Cannot be Empty");
                t1.requestFocus();
                }
            else
            if(!verifyfrom(t1.getText()))
            {
               JOptionPane.showMessageDialog(rootPane, "Enter a Valid Email id");
                t1.requestFocus();
            }

        }
        if(e.getSource()==t2 && !flagto)
        {
            if(t2.getText().equalsIgnoreCase("") ||t2.getText().equalsIgnoreCase(" "))
            {
                JOptionPane.showMessageDialog(rootPane, "To Cannot be Empty");
                t2.requestFocus();
                }
            else
            {
                String ping=t2.getText();
                String[] fi=ping.split("\\ ");
                int len=fi.length;
                for(int y=0;y<len;y++)
                    if(!verifyfrom(fi[y]))
                    {
                        JOptionPane.showMessageDialog(rootPane, "Enter Valid Email id");
                        t2.requestFocus();
                    }
            }

        }
    }

       boolean verifyfrom(String str)
       {
           boolean t=Pattern.matches("^([a-zA-Z0-9]+)([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-]+)([\\.]+)([a-zA-Z0-9\\._-]+)", str);
           return t;
       }

}
