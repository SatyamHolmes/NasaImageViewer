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
	private final String feedurl="https://www.nasa.gov/rss/dyn/image_of_the_day.rss";
	private static final int cachelimit=5;

	private JFrame frame;
	private ImagePanel panel;
	private JTextArea des;
	private Store<Item> item;
	private  URL url=null;
	private NodeList nlist;
	private BufferedImage backImage=null;

	private int index;
	private int clip;
	private boolean reqimage;

	public static void main(String[] arg)
	{
		NasaDailyImage work=new NasaDailyImage();
		work.getFeed();
		
		for(work.index=work.index; work.index<cachelimit; work.index++)
		{
			work.item.addFront(work.parseFeed(work.index));
		System.out.println(work.item.size());

			if(work.index==0)
			{
				work.item.removeLast();
				work.des.setText("\n"+work.item.get(work.clip).description+"\n\nPress DOWN direction key to download");	
				work.panel.repaint();
			}
		}

	}

	public NasaDailyImage()
	{
		try
		{
		index=0;
		clip=1;

		item=new Store<Item>();
		item.addFront(new Item("Title", "Date", ImageIO.read(new File("../fill.jpg")), "Welcome user. You are about to view some awesome images from NASA. Press LEFT and RIGHT direction key of keyboard to navigate and DOWN direction key to download the image that touches your heart."));
		backImage=ImageIO.read(new File("../semiback.png"));

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
		des.setText("\n"+item.get(clip).description);	

		url=new URL(feedurl);

		panel.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_LEFT)
				{
					if(index!=0 && clip!=1)
					{
						System.out.println(clip);
						clip--;
						des.setText("\n"+item.get(clip).description+"\n\nPress DOWN direction key to download");	
						panel.repaint();
						
						/*if(clip==1 && index>4 && !reqimage)
						{
		System.out.println(item.size()+" "+index);

							Thread t=new Thread(new Runnable(){
								public void run()
								{
									item.removeFront();
									item.addLast(parseFeed(index-5));
									clip++;
		System.out.println(item.size());

								}
							});
							t.start();
						}*/
					}
					else
					{
						if(index==0)
							des.append("\n\nThat's over for today. Come tommorrow for more");
						else
							des.append("\n\nSlow internet ehh..Please wait.. Loading the awesome image...");
					}
					
				}
				else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
				{
					if(index!=nlist.getLength()-1 && index!=0 && item.size()!=clip)
					{
						clip++;
						des.setText("\n"+item.get(clip).description+"\n\nPress DOWN direction key to download");	
						panel.repaint();
						if(clip==5 && !reqimage)
						{
		System.out.println(item.size());

							Thread t=new Thread(new Runnable(){
								public void run()
								{
									index++;
									item.removeLast();
									clip--;
									item.addFront(parseFeed(index));
		System.out.println(item.size());
								
								}
							});
							t.start();
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
					try
					{
						ImageIO.write(item.get(clip).image,"JPG",new File(item.get(clip).title+"jpg"));
						des.append("\nImage Downloaded");

					}
					catch(Exception ex)
					{

					}
				}
						

			}
		});
	}
	catch(Exception e){
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
			des.append("\nNo internet connection!!");
		}
	}

	synchronized public Item parseFeed(int i)
	{
		Element em;
			try
			{
			System.out.println("reading");

				reqimage=true;
				em=(Element)nlist.item(i);
				StringBuffer str=new StringBuffer(((Element)em.getElementsByTagName("enclosure").item(0)).getAttribute("url"));
				str.insert(4,'s');
				reqimage=false;
				return new Item(em.getElementsByTagName("title").item(0).getTextContent(),em.getElementsByTagName("pubDate").item(0).getTextContent(),ImageIO.read(new URL(new String(str))),em.getElementsByTagName("description").item(0).getTextContent());

			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		
	}

	class ImagePanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{
			try
			{
				g.drawImage(item.get(clip).image,0,0,1000,700,this);
				g.drawImage(backImage,0,0,1000,700,this);
				g.setColor(Color.WHITE);
				g.setFont(new Font("TimesRoman",Font.BOLD,25));
				g.drawString(item.get(clip).title,250-(7*(item.get(clip).title.length()-32)),30);
				g.setFont(new Font("TimesRoman",Font.ITALIC,15));
				g.drawString(item.get(clip).date,700,55);
				g.drawImage(item.get(clip).image,10,80,980,460,this);
				Toolkit.getDefaultToolkit().sync();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}