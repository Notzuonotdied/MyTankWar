package GameScreen;

import Tank.Bomb;
import Tank.Monster;
import Tank.MyTank;
import Tank.Recorder;
import UIElement.BlockWall;
import UIElement.CommonWall;
import UIElement.Tree;

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
    // 设置关卡
    public static int level = 0;
    // 控制界面是否移除的变量
    public static boolean continueBtn = false;
    public static boolean buttonWin = false;
    public static boolean buttonFail = false;

    // 总成绩
    private static int score = 0;
    // 控制是否在我的坦克死亡后继续游戏的变量
    private static boolean choice = false;

    /**
     * GamePanel构造函数
     */
    GamePanel(GamePanelStatus flag) {
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

            if (size != 0 && flag == GamePanelStatus.Continue) {
                // 恢复数据
                recorder.Restore();
            } else {
                MyTank.getInstance().createMyTank();
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
        MyTank.getInstance().drawMyTank(g);
        // 画出敌人坦克
        DrawMonster(g);
        // 画出墙,爆炸效果，树木，普通墙
        DrawItem(g);
        // 信息显示
        DrawInfo(g);
    }

    // 画出怪物
    private void DrawMonster(Graphics g) {
        score = Monster.getInstance().drawAllMonster(g, score);
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
        if (Monster.getInstance().getMonsterSize() == 0 && MyTank.getInstance().isLive) {
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
        if (!MyTank.getInstance().isLive) {
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
        MyTank.getInstance().keyPressed(e);
        // 必须调用this.repaint()函数，来重新绘制界面
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

