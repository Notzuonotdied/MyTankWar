package Hero;

import java.awt.*;
import java.util.Vector;

import static Util.CommonUtil.size;

public class Bomb {
    private static final Vector<Bomb> bombs = new Vector<>();
    private static int step = 0;
    // 爆炸效果图片初始化
    private static Image[] BombImages = null;
    private static Bomb bomb;

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
    private boolean isLive = true;
    // 定义爆炸效果的左上角坐标（x,y）
    private int x;
    private int y;
    private Bomb() {
    }

    Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Bomb getInstance() {
        if (bomb == null) {
            synchronized (Bomb.class) {
                if (bomb == null) {
                    bomb = new Bomb();
                }
            }
        }
        return bomb;
    }

    public void add2Bombs(Bomb bomb) {
        bombs.add(bomb);
    }

    /**
     * 画出完整的爆炸效果
     *
     * @param graphics 画笔
     */
    public void drawAllBomb(Graphics graphics) {
        // 画出爆炸效果
        for (int i = 0; i < bombs.size(); i++) {
            bomb = bombs.get(i);
            if (bomb.isLive) {
                bomb.drawBomb(graphics);
            } else {
                bombs.remove(bomb);
            }
        }
    }

    /**
     * 画出爆炸效果
     *
     * @param g 画笔
     */
    private void drawBomb(Graphics g) {
        try {
            Thread.sleep(50);
            if (step == BombImages.length) {
                bombs.remove(bomb);
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
