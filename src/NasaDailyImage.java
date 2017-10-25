import java.util.ArrayList;
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
	private final String feedurl="http://www.nasa.gov/rss/dyn/image_of_the_day.rss";
	private final int cachelimit=5;
	private int cachenum;
	private JFrame frame;
	private ImagePanel panel;
	private JTextArea des;
	private ArrayList<String> title;
	private ArrayList<String> date;
	private ArrayList<String> description;
	private  URL url=null;
	private ArrayList<Image> image;
	private NodeList nlist;
	private int index;
	private static boolean start;

	public static void main(String[] arg)
	{
		NasaDailyImage work=new NasaDailyImage();
		work.getFeed();
		work.parseFeed();
		
	}

	public NasaDailyImage()
	{
		index=0;
		cachenum=0;
		start=false;

		title=new ArrayList<String>();
		date=new ArrayList<String>();
		description=new ArrayList<String>();
		image=new ArrayList<Image>();

		title.add("Title");
		date.add("Date");
		description.add("Welcome user. You are about to view some awesome images from NASA. Press LEFT and RIGHT direction key of keyboard to navigate and DOWN direction key to download the image that touches your heart.");
		image.add(new ImageIcon("../fill.jpg").getImage());

		frame=new JFrame();
		des=new JTextArea();
		panel=new ImagePanel();

		frame.setSize(1000,700);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		panel.setLayout(null);
		panel.setOpaque(true);
		panel.setFocusable(true);
		panel.requestFocusInWindow();
		des.setBounds(10,540,980,690);
		panel.add(des);
		des.setOpaque(false);
		des.setEditable(false);
		des.setLineWrap(true);
		des.setWrapStyleWord(true);
		des.setForeground(Color.WHITE);
		des.setText("\n"+description.get(index));	


		try
		{
			url=new URL(feedurl);

			panel.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e)
				{
					
					if(e.getKeyCode()==KeyEvent.VK_LEFT)
					{
						if(index!=0 && index!=1)
							index--;
					}
					else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
					{
						if(index!=nlist.getLength() && index!=0 && image.size()!=(index+1))
						{
							index++;
							des.setText("\n"+description.get(index)+"\n\nPress DOWN direction key to download");	
							if(cachenum==cachelimit)
							{
								cachenum=0;
								//	notify();
							}
						}
						else
						{
							if(index==nlist.getLength())
							{
								des.append("\n\nThat's over for today. Come tommorrow for more");
							}
							else
							{
								des.append("\n\nSlow internet ehh..Please wait.. Loading the awesome image...");
							}
						}
					}
					else if(e.getKeyCode()==KeyEvent.VK_DOWN)
					{
				
					}
					panel.repaint();
				}
			});
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
			des.append("\n\nConnecting to the NASA...");
			InputStream s=url.openStream();
			des.append("\nParsing the feed...");
			DocumentBuilderFactory dfac=DocumentBuilderFactory.newInstance();
			DocumentBuilder dbuild=dfac.newDocumentBuilder();
			Document doc=dbuild.parse(s);
			doc.getDocumentElement().normalize();
			nlist=doc.getElementsByTagName("item");
			des.append("\nGetting the awesome image...");
		}
		catch(Exception e)
		{
			des.append("\nNo internet connection");
		}
	}

	public void parseFeed()
	{
		Element em;
		for(int i=0;i<nlist.getLength();i++)
		{
			try
			{
				if(cachenum==cachelimit)
				{
				//	wait();
				}

				em=(Element)nlist.item(i);
				title.add(em.getElementsByTagName("title").item(0).getTextContent());
				date.add(em.getElementsByTagName("pubDate").item(0).getTextContent());
				
				StringBuffer str=new StringBuffer(((Element)em.getElementsByTagName("enclosure").item(0)).getAttribute("url"));
				str.insert(4,'s');
				image.add(ImageIO.read(new URL(new String(str))));
				
				description.add(em.getElementsByTagName("description").item(0).getTextContent());
				
				if(index==0)
				{
					index++;
					des.setText("\n"+description.get(index)+"\n\nPress DOWN direction key to download");	
					panel.repaint();
				}

				cachenum++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	class ImagePanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{
				g.drawImage(new ImageIcon(image.get(index)).getImage(),0,0,1000,700,this);
				g.drawImage(new ImageIcon("../semiback.png").getImage(),0,0,1000,700,this);
				g.setColor(Color.WHITE);
				g.setFont(new Font("TimesRoman",Font.BOLD,25));
				g.drawString(title.get(index),250,30);
				g.setFont(new Font("TimesRoman",Font.ITALIC,15));
				g.drawString(date.get(index),700,55);
				g.drawImage(new ImageIcon(image.get(index)).getImage(),10,80,980,460,this);
		}
	}
}