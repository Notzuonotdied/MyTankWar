import java.awt.*;

import static Util.CommonUtil.size;

class Bomb {
    private static int step = 0;
    // 爆炸效果图片初始化
    // 定义为全局静态变量
    private static Image[] BombImages = null;

    static {
        try {
            BombImages = new Image[]{
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
            g.drawImage(BombImages[step], x - size / 2, y - size, 2 * size, 2 * size, null);
            step++;
        } catch (Exception e) {
            System.out.println("输出图片出现问题了！");
            e.printStackTrace();
        }
    }
}
