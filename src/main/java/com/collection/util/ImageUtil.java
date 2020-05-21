package com.collection.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("restriction")
public class ImageUtil {

	public static void main(String[] args) {
		// 把字加入到图片中demo
		// createImageDemo();
		// 几张图片合在一起demo
		// combinationimageDemo();
		reduceImg("H:\\IMG_1523.JPG", "H:\\IMG_1523new.JPG", 460, 768);

	}

	private static void combinationimageDemo() {
		List<String> paths = new ArrayList<String>();
		String outPath1 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color71.jpg";
		String outPath2 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color72.jpg";
		String outPath3 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color73.png";
		String outPath4 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color74.png";
		paths.add(outPath1);
		paths.add(outPath2);
		paths.add(outPath3);
		paths.add(outPath4);
		String outPath = "E:\\4.jpg";
		try {
			getCombinationOfhead(paths, outPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void createImageDemo() {
		String filePath = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color01.jpg";
		String outPath1 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color71.jpg";
		String outPath2 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color72.jpg";
		String outPath3 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color73.png";
		String outPath4 = "F:\\hoheng\\c餐饮大师\\logo\\bg_color\\bg_color74.png";
		drawTextInImg(filePath, outPath1, new FontText("孙", "#FFF", 50, "黑体"), 25, 65);
		drawTextInImg(filePath, outPath2, new FontText("周一", "#FFF", 40, "黑体"), 10, 65);
		drawTextInImg(filePath, outPath3, new FontText("李超", "#FFF", 40, "黑体"), 10, 65);
		drawTextInImg(filePath, outPath4, new FontText("周", "#FFF", 50, "黑体"), 25, 65);
	}

	/**
	 * 随机生成背景不同的默认头像
	 * 
	 * @param username
	 *            用户名称
	 * @return
	 * @throws IOException
	 */
	public static String randomlyGeneratedDefaultAvatar(String username, String userid, String getRealPath,
			String fileDirectory) throws IOException {
		File file = new File(getRealPath + fileDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 设置用户默认头像名称
		userid = "systemDefaultUserHeadimg" + userid;
		// 默认图片的保存路径
		String outPath = getRealPath + fileDirectory + "/" + userid + ".jpg";
		// 获取背景图片
		String filePath = getRealPath + randomChangeBackgroundImage();
		// 判断姓名的长度
		int namelength = username.length();
		if (namelength > 2) {
			String imgname = "";
			// 判断是否是中文
			boolean fag = CharUtil.isChinese(username);
			if (fag) {// 是中文
				imgname = username.substring(0, 1);
				// 生成默认头像
				drawTextInImg(filePath, outPath, new FontText(imgname, "#FFF", 50, "黑体"), 26, 66);
			} else {
				imgname = username.substring(0, 2).toUpperCase();
				// 生成默认头像
				drawTextInImg(filePath, outPath, new FontText(imgname, "#FFF", 50, "黑体"), 23, 68);
			}
		} else {
			if (namelength == 2) {
				// 生成默认头像
				drawTextInImg(filePath, outPath, new FontText(username, "#FFF", 40, "黑体"), 8, 65);
			} else {
				// 生成默认头像
				drawTextInImg(filePath, outPath, new FontText(username, "#FFF", 50, "黑体"), 26, 66);
			}
		}
		return fileDirectory + "/" + userid + ".jpg";
	}

	/**
	 * 随机获取背景图片
	 * 
	 * @return
	 */
	public static String randomChangeBackgroundImage() {
		/*
		 * String [] backgroundimages =
		 * {"/images/bg_color1.png","/images/bg_color2.png",
		 * "/images/bg_color3.png",
		 * "/images/bg_color4.png","/images/bg_color5.png",
		 * "/images/bg_color6.png","/images/bg_color7.png"};
		 */
		String[] backgroundimages = { "/images/bg_color1.jpg", "/images/bg_color2.jpg", "/images/bg_color3.jpg",
				"/images/bg_color4.jpg", "/images/bg_color5.jpg", "/images/bg_color6.jpg", "/images/bg_color7.jpg" };
		Random rd = new Random();
		int num = rd.nextInt(7);
		return backgroundimages[num];
	}

	public static void drawTextInImg(String filePath, String outPath, FontText text, Integer left, Integer top) {
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image img = imgIcon.getImage();
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = bimage.createGraphics();
		g.setColor(getColor(text.getWm_text_color()));
		g.setBackground(Color.RED);
		g.drawImage(img, 0, 0, null);
		Font font = null;
		if (null != text.getWm_text_font() && !"".equals(text.getWm_text_font()) && text.getWm_text_size() != null) {
			font = new Font(text.getWm_text_font(), Font.BOLD, text.getWm_text_size());
		} else {
			font = new Font(text.getWm_text_font(), Font.BOLD, 15);
		}

		System.out.println(font.getSize());
		g.setFont(font);
		FontMetrics metrics = new FontMetrics(font) {
		};
		Rectangle2D bounds = metrics.getStringBounds(text.getText(), null);
		int textWidth = (int) bounds.getWidth();
		int textHeight = (int) bounds.getHeight();
		if (left == null) {
			left = (width - textWidth) / 2;
		}
		if (top == null) {
			top = height - textHeight;
		}

		g.drawString(text.getText(), left, top);
		g.dispose();

		try {
			FileOutputStream out = new FileOutputStream(outPath);
			// bimage.set
			ImageIO.write(bimage, "png", out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void drawTextInImg(String filePath, String outPath, FontText text) {
		drawTextInImg(filePath, outPath, text, null, null);
	}

	// color #2395439
	public static Color getColor(String color) {
		if (color.charAt(0) == '#') {
			color = color.substring(1);
		}
		if (color.length() != 6) {
			return null;
		}
		try {
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	BufferedImage image = null;

	public void createImage(String fileLocation) {
		try {
			FileOutputStream fos = new FileOutputStream(fileLocation);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
			encoder.encode(image);
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void graphicsGeneration(String name, String id, String classname, String imgurl) {

		int imageWidth = 500;// 图片的宽度

		int imageHeight = 400;// 图片的高度

		image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.blue);
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		graphics.setColor(Color.ORANGE);
		graphics.setFont(new Font("黑体", Font.BOLD, 20));
		graphics.drawString("姓名 : " + name, 50, 75);
		graphics.drawString("学号 : " + id, 50, 150);
		graphics.drawString("班级 : " + classname, 50, 225);
		// ImageIcon imageIcon = new ImageIcon(imgurl);
		// graphics.drawImage(imageIcon.getImage(), 230, 0, null);

		// 改成这样:
		BufferedImage bimg = null;
		try {
			bimg = javax.imageio.ImageIO.read(new java.io.File(imgurl));
		} catch (Exception e) {
		}

		if (bimg != null)
			graphics.drawImage(bimg, 230, 0, null);
		graphics.dispose();

		createImage(imgurl);

	}

	/**
	 * 生成组合头像
	 * 
	 * @param paths
	 *            用户图像
	 * @throws IOException
	 */
	public static void getCombinationOfhead(List<String> paths, String outPath) throws IOException {
		List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
		// 压缩图片所有的图片生成尺寸同意的 为 50x50
		for (int i = 0; i < paths.size(); i++) {
			bufferedImages.add(resize2(paths.get(i), 50, 50, true));
		}

		int width = 115; // 这是画板的宽高

		int height = 115; // 这是画板的高度

		// BufferedImage.TYPE_INT_RGB可以自己定义可查看API

		BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 生成画布
		Graphics g = outImage.getGraphics();

		Graphics2D g2d = (Graphics2D) g;

		// 设置背景色
		g2d.setBackground(new Color(255, 255, 255));

		// 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
		g2d.clearRect(0, 0, width, height);

		// 开始拼凑 根据图片的数量判断该生成那种样式的组合头像目前为4中
		int j = 1;
		for (int i = 1; i <= bufferedImages.size(); i++) {
			if (bufferedImages.size() == 4) {
				if (i <= 2) {
					g2d.drawImage(bufferedImages.get(i - 1), 50 * i + 4 * i - 50, 4, null);
				} else {
					g2d.drawImage(bufferedImages.get(i - 1), 50 * j + 4 * j - 50, 58, null);
					j++;
				}
			} else if (bufferedImages.size() == 3) {
				if (i <= 1) {

					g2d.drawImage(bufferedImages.get(i - 1), 31, 4, null);

				} else {

					g2d.drawImage(bufferedImages.get(i - 1), 50 * j + 4 * j - 50, 58, null);

					j++;
				}

			} else if (bufferedImages.size() == 2) {

				g2d.drawImage(bufferedImages.get(i - 1), 50 * i + 4 * i - 50, 31, null);

			} else if (bufferedImages.size() == 1) {

				g2d.drawImage(bufferedImages.get(i - 1), 31, 31, null);

			}

			// 需要改变颜色的话在这里绘上颜色。可能会用到AlphaComposite类
		}

		String format = "JPG";

		ImageIO.write(outImage, format, new File(outPath));
	}

	/**
	 * 图片缩放
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 */
	public static BufferedImage resize2(String filePath, int height, int width, boolean bb) {
		try {
			double ratio = 0; // 缩放比例
			File f = new File(filePath);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				// copyimg(filePath, "D:\\img");
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
			return (BufferedImage) itemp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 压缩图片
	 * @param imgsrc 源文件路径
	 * @param imgdist 目标文件路径
	 * @param widthdist 目标宽度
	 * @param heightdist 目标高度
	 */
	public static void reduceImg(String imgsrc, String imgdist, int widthdist, int heightdist) {
		try {
			File srcfile = new File(imgsrc);
			Image src = javax.imageio.ImageIO.read(srcfile);
			BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
			FileOutputStream out = new FileOutputStream(imgdist);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}