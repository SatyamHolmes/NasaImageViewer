import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.net.URL;
import java.awt.image.*;
import javax.imageio.*;

class NasaDailyImage
{
	private JFrame frame;
	private ImagePanel panel;
	private String title;
	private String date;
	private  URL url=null;
	private URL image=null;
	private NodeList nlist;
	private int index;

	public static void main(String[] arg)
	{
		NasaDailyImage work=new NasaDailyImage();
		work.getFeed();
		work.nextFeed();	
	}

	public NasaDailyImage()
	{
		frame=new JFrame();
		panel=new ImagePanel();
		title="Title";
		date="Date";
		
		frame.setSize(1000,700);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		panel.setLayout(null);
		panel.setOpaque(true);
	
		index=0;

		try
		{
			url=new URL("http://www.nasa.gov/rss/dyn/image_of_the_day.rss");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getFeed()
	{
		try
		{
			DocumentBuilderFactory dfac=DocumentBuilderFactory.newInstance();
			DocumentBuilder dbuild=dfac.newDocumentBuilder();
			Document doc=dbuild.parse(url.openStream());
			doc.getDocumentElement().normalize();
			nlist=doc.getElementsByTagName("item");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void nextFeed()
	{
		Element em;
		em=(Element)nlist.item(index);
		title=em.getElementsByTagName("title").item(0).getTextContent();
		date=em.getElementsByTagName("pubDate").item(0).getTextContent();
		StringBuffer str=new StringBuffer(((Element)em.getElementsByTagName("enclosure").item(0)).getAttribute("url"));
		str.insert(4,'s');
		try
		{
			image=new URL(new String(str));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		panel.repaint();
	}

	class ImagePanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{
			try
			{
				g.drawImage(new ImageIcon("back.png").getImage(),0,0,1000,700,this);
				g.drawString(title,400,10);
				g.drawString(date,600,50);
				BufferedImage bf=ImageIO.read(image);
				g.drawImage(new ImageIcon(bf).getImage(),10,100,970,500,this);
			}
			catch(Exception e)
			{
				System.out.println("its here");
			}
		}
	}
}

