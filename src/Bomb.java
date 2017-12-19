import java.awt.*;

class Bomb {
    static int step = 0;
    // 爆炸效果图片初始化
    // 定义为全局静态变量
    private static Image[] BombImages = null;

    static {
        try {
            BombImages = new Image[]{
                    // ImageIO.read(new File("bomb1.gif")),
                    // ImageIO.read(new File("bomb2.gif")),
                    // ImageIO.read(new File("bomb3.gif")),
                    // ImageIO.read(new File("bomb4.gif")),
                    // ImageIO.read(new File("bomb5.gif")),
                    // ImageIO.read(new File("bomb6.gif")),
                    // ImageIO.read(new File("bomb7.gif")),
                    // ImageIO.read(new File("bomb8.gif")),
                    // ImageIO.read(new File("bomb.gif")),};

                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/1.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/2.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/3.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/4.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/5.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/6.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/7.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/8.png")),
                    Toolkit.getDefaultToolkit().getImage(
                            Bomb.class.getResource("/images/8.png")),};
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 爆炸效果是否还存在，true为存在，false为不存在
    boolean isLive = true;
    Graphics g;
    // 定义爆炸效果的左上角坐标（x,y）
    private int x;
    private int y;
    // 构造函数
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 画出爆炸效果
    public void drawBomb(Graphics g) {
        try {
            Thread.sleep(50);
            if (step == BombImages.length) {
                GamePanel.bombs.remove(GamePanel.bomb);
                step = 0;
                this.isLive = false;
            }
            // System.out.println("画出爆炸效果！");
            g.drawImage(BombImages[step], x - TankMember.size / 2, y
                            - TankMember.size, 2 * TankMember.size,
                    2 * TankMember.size, null);
            step++;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("输出图片出现问题了！");
            e.printStackTrace();
        }
    }
}
