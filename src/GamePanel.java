import UIElement.BlockWall;
import UIElement.CommonWall;
import UIElement.Tree;
import Util.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;

import static Util.CommonUtil.screenWidth;

@SuppressWarnings("serial")
// 游戏面板
public class GamePanel extends JPanel implements KeyListener, Runnable {
    // 定义我的坦克可以连发的子弹数
    private static final int buttleNumber = 2;
    // 设置关卡
    public static int level = 0;
    // 控制界面是否移除的变量
    public static boolean button = false;
    public static boolean buttonWin = false;
    public static boolean buttonFail = false;
    // 定义我的坦克类
    public static MyTank myTank = null;

    // 总成绩
    private static int score = 0;
    // 控制是否在我的坦克死亡后继续游戏的变量
    private static boolean choice = false;


    /**
     * GamePanel构造函数
     */
    GamePanel(int flag) {
        int size = 0;
        Recorder recorder = new Recorder();
        // 判断记录的文本是否为空
        try {
            // 判读文本是否存在，不存在就新建一个空白文本
            File f = new File(recorder.getFileName());
            if (f.exists()) {
                FileInputStream fin = new FileInputStream(recorder.getFileName());
                // 获取
                size = fin.available();
                // 已经不需要判断了，关闭流
                fin.close();
            } else {
                if (f.createNewFile()) {
                    System.out.println("文件创建成功！");
                } else {
                    System.out.println("文件创建失败！");
                }
            }

            if (size != 0 && flag == 1) {
                // 恢复数据
                recorder.ReInfo();

            } else {
                // 创建我的坦克,设置坦克的位置
                myTank = new MyTank(600, 500);
                myTank.setDirect(Direction.UP);
                Monster.getInstance().restoreMonsters();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    // 画笔函数
    public void paint(Graphics g) {
        super.paint(g);
        // 初始化背景图片
        Image Background = Toolkit.getDefaultToolkit().getImage(
                GamePanel.class.getResource("/images/background.gif"));
        g.drawImage(Background, 35, -160, screenWidth, screenWidth + 80, null);

        // 画出我的坦克
        DrawMyTank(g);
        // 画出敌人坦克
        DrawMonster(g);
        // 画出墙,爆炸效果，树木，普通墙
        DrawItem(g);
        // 信息显示
        DrawInfo(g);
    }

    // 画出我的坦克
    private void DrawMyTank(Graphics g) {
        // 画出我自己的坦克
        if (myTank.isLive) {
            myTank.drawTank(myTank.x, myTank.y, g, 0);
            this.repaint();
        }

        // 画出子弹
        for (int i = 0; i < myTank.bullets.size(); i++) {
            Bullet bullet = myTank.bullets.get(i);
            if (bullet != null && bullet.isLive && myTank.isLive
                    && bullet.BulletComeAcrossCWall(bullet)
                    && !bullet.BulletComeAcrossMonster(bullet)
                    && bullet.BulletComeAcrossBWall(bullet, myTank.getBlockWall())) {
                // 画出子弹的轨迹
                bullet.drawBullet(g);
                // 判断是否集中了怪物
                bullet.HitMonster();
            }
            assert bullet != null;
            if (!bullet.isLive) {
                // 如果子弹存在状态为假就移除子弹
                myTank.bullets.remove(bullet);
            }
        }
    }

    // 画出怪物
    private void DrawMonster(Graphics g) {
        score = Monster.getInstance().drawAllMonster(this, g, score);
    }

    /**
     * 画出墙,爆炸效果，树木，普通墙
     */
    private void DrawItem(Graphics graphics) {
        CommonWall.getInstance().drawAllCWall(graphics);
        BlockWall.getInstance().drawAllBWall(graphics);
        Tree.getInstance().drawAllTree(graphics);
        Bomb.getInstance().drawAllBomb(graphics);
    }

    /**
     * 信息显示
     *
     * @param g 画笔
     */
    private void DrawInfo(Graphics g) {
        // 左上角文字
        g.setColor(Color.black);
        g.setFont(new Font("楷体", Font.BOLD, 18));
        g.drawString("坦克大战-无聊版", 35, 33);
        // 正上方中间文字
        g.setFont(new Font("楷体", Font.BOLD, 23));
        g.drawString("区域内还有敌方坦克: ", 280, 23);
        g.drawString("总成绩： ", 550, 23);
        // 提示信息文字属性设置
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.ITALIC, 30));
        g.drawString("" + Monster.getInstance().getMonsterSize(), 520, 23);
        g.drawString("" + GamePanel.score, 640, 23);

        // 提示信息
        if (Monster.getInstance().getMonsterSize() == 0 && myTank.isLive) {
            Font f = g.getFont();
            // 判断是否赢得比赛--赢了
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.drawString("You are Winner！ ", 175, 310);
            g.setFont(f);
            // 选择方案
            GamePanel.buttonWin = true;
            if (level == 3) {
                g.drawString("You are Crazy！ ", 175, 370);
            }
        }
        if (!myTank.isLive) {
            Font f = g.getFont();
            // 判断是否赢得比赛--输了
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.drawString("You are lost！ ", 180, 310);
            g.setFont(f);
            // 选择方案
            GamePanel.buttonFail = true;
        }
    }

    // 响应键盘
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            myTank.setDirect(Direction.DOWN);
            this.repaint();
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            // 设置我的坦克的方向
            myTank.setDirect(Direction.UP);
            this.repaint();
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            myTank.setDirect(Direction.LEFT);
            this.repaint();
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            myTank.setDirect(Direction.RIGHT);
            this.repaint();
            myTank.move();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (myTank.bullets.size() < buttleNumber) {
                myTank.ShotEnemy();
            }
        }
        // 必须调用this.repain()函数，来重新绘制界面
        this.repaint();
    }

    public void keyReleased(KeyEvent e) {

    }

    public void run() {
        // 刷新界面
        while (!GamePanel.choice) {
            try {
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }
}

