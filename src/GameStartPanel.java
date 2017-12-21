import Util.Audio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static Util.CommonUtil.*;

/**
 * 开始面板
 */
@SuppressWarnings("serial")
class GameStartPanel extends JPanel implements KeyListener {

    // 返回值
    public static boolean button0 = false;
    public static boolean button1 = false;
    // 图片类
    private int flag = 1;
    // 初始化图片坦克的图片
    private Image[] tankImages = new Image[]{
            Toolkit.getDefaultToolkit().getImage(
                    TankMember.class.getResource("/images/1.gif")),
            Toolkit.getDefaultToolkit().getImage(
                    TankMember.class.getResource("/images/m0.gif"))
    };
    // 设置开始游戏继续游戏的位置
    private int InfoX = 320;
    private int InfoY = 400;
    private int x = InfoX - size / 2 * 3 - 8;
    private int y = InfoY - 30;

    public void paint(Graphics g) {
        super.paint(g);
        // 画出信息代码部分
        DrawInfo(g);
    }

    // 画出信息
    private void DrawInfo(Graphics g) {

        g.fillRect(0, 0, screenWidth + 10, screenHeight);
        // 提示信息

        g.setColor(Color.YELLOW);
        // 开关信息的字体
        Font myfont1 = new Font("楷体", Font.BOLD, 60);
        g.setFont(myfont1);
        g.drawString("坦克大战-无聊版", 180, 310);
        g.setColor(Color.RED);
        g.drawString("坦克大战-无聊版", 176, 315);

        g.setColor(Color.GRAY);
        Font myfont = new Font("楷体", Font.BOLD, 30);
        g.setFont(myfont);

        g.drawString("开始游戏", InfoX, InfoY);
        g.drawString("继续游戏", InfoX, InfoY + 40);
        // 画出坦克
        g.drawImage(tankImages[this.flag], this.x, this.y, null);
        this.repaint();

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_UP:
                if (flag == 1) {
                    this.x = InfoX - size / 2 * 3;
                    this.y = InfoY + 15;
                    this.flag = 0;
                } else {
                    this.x = InfoX - size / 2 * 3 - 8;
                    this.y = InfoY - 30;
                    this.flag = 1;
                }
                this.repaint();
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                if (this.flag == 0) {
                    GameStartPanel.button0 = true;
                } else {
                    GameStartPanel.button1 = true;
                }
                // 启动声音
                new Audio("bgm.wav").start();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {

    }

}

