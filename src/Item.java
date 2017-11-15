import java.awt.*;
import java.awt.image.*;


class Item
{
	public String title;
	public String date;
	public BufferedImage image;
	public String description;

	public Item(String title,String date,BufferedImage image,String description)
	{
		this.title=title;
		this.date=date;
		this.image=image;
		this.description=description;
	}
}