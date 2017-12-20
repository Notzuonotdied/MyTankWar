import UIElement.BlockWall;
import UIElement.CommonWall;
import UIElement.Tree;
import Util.CommonUtil;
import Util.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.Vector;

@SuppressWarnings("serial")
// 游戏面板
public class GamePanel extends JPanel implements KeyListener, Runnable {
    // 定义我的坦克可以连发的子弹数
    private static final int buttleNumber = 2;
    // 设置关卡
    public static int level = 0;
    // 定义怪物的数量
    public static int monsterSize = 6;
    // 控制界面是否移除的变量
    public static boolean button = false;
    public static boolean buttonWin = false;
    public static boolean buttonFail = false;
    // 定义我的坦克类
    public static MyTank myTank = null;
    // 定义一个敌人的坦克集合
    public static Vector<Monster> monster = new Vector<>();
    // 定义一个爆炸集合
    public static Bomb bomb = null;
    public static Vector<Bomb> bombs = new Vector<>();
    public static Vector<CommonWall> CWalls = new Vector<>();
    // 定义一个内部石墙集合
    public static Vector<BlockWall> BWalls_1 = new Vector<>();
    // 总成绩
    private static int score = 0;
    // 控制是否在我的坦克死亡后继续游戏的变量
    private static boolean choice = false;
    private Vector<BlockWall> BWalls = new Vector<>();
    private Vector<Tree> tree = new Vector<>();

    // GamePanel构造函数
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
                // 创建怪物的坦克
                for (int i = 0; i < GamePanel.monsterSize; i++) {
                    // 定义坦克的位置
                    Random r = new Random();
                    int moveSteps = r.nextInt(30) + 35;
                    Monster mon = new Monster((i + 1) * 80, moveSteps);
                    Monster.setTankNumber(monsterSize);
                    mon.setDirect(Direction.DOWN);
                    // 启动敌人坦克
                    CommonUtil.getInstance().startCachedThread(mon);
                    // 将敌人坦克添加到坦克集合中
                    monster.add(mon);
                    // 为敌人添加子弹
                    Bullet bullet = new Bullet(
                            mon.getX() + TankMember.size / 2, mon.getY()
                            + TankMember.size / 2, Direction.DOWN);
                    // 加到子弹集合中
                    mon.bullets.add(bullet);
                    // 启动子弹线程
                    CommonUtil.getInstance().startCachedThread(bullet);
                }
                // 初始化普通墙
                DrawCWall();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        // 初始化石墙和树木
        Initialization();
    }

    // 画笔函数
    public void paint(Graphics g) {
        super.paint(g);
        // 初始化背景图片
        Image Background = Toolkit.getDefaultToolkit().getImage(
                GamePanel.class.getResource("/images/background.gif"));
        g.drawImage(Background, 35, -160, MainFrame.screenWidth,
                MainFrame.screenWidth + 80, null);

        // 画出我的坦克
        DrawMyTank(g);
        // 画出敌人坦克
        DrawMonster(g);
        // 画出墙,爆炸效果，树木，普通墙
        DrawItem(g);
        // 信息显示
        DrawInfo(g);
    }

    // 初始化普通墙
    private void DrawCWall() {
        // 初始化普通墙
        for (int i = 0; i < 15; i++) {
            if (i < 12) {
                CWalls.add(new CommonWall(CommonWall.commonWallWidth
                        * (i + 4), 158));
                CWalls.add(new CommonWall(CommonWall.commonWallWidth
                        * (i + 20), 158));
            }
            CWalls.add(new CommonWall(154, CommonWall.commonWallHeight
                    * (i + 8)));
            CWalls.add(new CommonWall(632, CommonWall.commonWallHeight
                    * (i + 8)));
        }

        for (int i = 0; i < 3; i++) {
            CWalls.add(new CommonWall(CommonWall.commonWallWidth * (i + 13),
                    215));
            CWalls.add(new CommonWall(CommonWall.commonWallWidth * (i + 13),
                    237));
            CWalls.add(new CommonWall(CommonWall.commonWallWidth * (i + 21),
                    215));
            CWalls.add(new CommonWall(CommonWall.commonWallWidth * (i + 21),
                    237));
        }
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
                    && bullet.BulletComeAcrossBWall(bullet)) {
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
        for (int i = 0; i < monster.size(); i++) {
            Monster mon = monster.get(i);
            if (mon.isLive) {
                mon.drawTank(mon.getX(), mon.getY(), g, 1);
                this.repaint();
                for (int j = 0; j < mon.bullets.size(); j++) {
                    // 取出一个子弹
                    Bullet bullet = mon.bullets.get(j);
                    if (bullet.isLive && bullet.BulletComeAcrossCWall(bullet)
                            && bullet.BulletComeAcrossBWall(bullet)) {
                        // 画出子弹的轨迹
                        bullet.drawBullet(g);
                        // 判断是否击中了我的坦克
                        bullet.HitMyTank();
                    }
                    if (!bullet.isLive) {
                        mon.bullets.remove(bullet);
                    }
                }
            }
            // 怪物死亡就移除
            if (!mon.isLive) {
                monster.remove(mon);
                GamePanel.score++;
            }
        }
    }

    /**
     * 画出墙,爆炸效果，树木，普通墙
     */
    private void DrawItem(Graphics g) {
        // 画出爆炸效果
        for (int i = 0; i < GamePanel.bombs.size(); i++) {
            bomb = GamePanel.bombs.get(i);
            if (bomb.isLive) {
                bomb.drawBomb(g);
            } else {
                bombs.remove(bomb);
            }
        }

        // 画出普通墙
        for (int i = 0; i < GamePanel.CWalls.size(); i++) {
            CommonWall cWall = GamePanel.CWalls.get(i);
            // 如果普通墙存在状态为真就画出来
            if (cWall.isLive) {
                cWall.drawCWall(g);
            } else {
                GamePanel.CWalls.remove(cWall);
            }
        }

        // 画出外围石墙
        for (BlockWall BWall : this.BWalls) {
            BWall.drawBWall(g);
        }
        // 内部石墙
        for (int i = 0; i < GamePanel.BWalls_1.size(); i++) {
            GamePanel.BWalls_1.get(i).drawBWall(g);
        }

        // 画出树木
        for (Tree aTree : this.tree) {
            aTree.drawTree(g);
        }
    }

    // 信息显示
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
        g.drawString("" + GamePanel.monster.size(), 520, 23);
        g.drawString("" + GamePanel.score, 640, 23);

        // 提示信息
        if (monster.size() == 0 && myTank.isLive) {
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

    // 初始化工作
    private void Initialization() {
        // 初始化除了普通墙外的所有墙体--注：其他的墙体都设置为无敌了，不可攻击
        // 初始化石墙-外部围墙
        for (int i = 0; i < 23; i++) {
            BWalls.add(new BlockWall(BlockWall.BlockWallWidth * i, 0));
            BWalls.add(new BlockWall(BlockWall.BlockWallWidth * i,
                    MainFrame.screenHeight - BlockWall.BlockWallHeight));
            BWalls.add(new BlockWall(0, BlockWall.BlockWallHeight * i));
            BWalls.add(new BlockWall(MainFrame.screenWidth
                    - BlockWall.BlockWallWidth, BlockWall.BlockWallHeight * i));
        }
        // 初始化石墙-内部围墙
        for (int i = 0; i < 4; i++) {
            BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 6),
                    BlockWall.BlockWallHeight * 5));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 13),
                    BlockWall.BlockWallHeight * 5));
        }
        // 初始化石墙-内部墙-上两个
        for (int i = 0; i < 5; i++) {
            if (i < 2) {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 5),
                        BlockWall.BlockWallHeight * 10));
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 16),
                        BlockWall.BlockWallHeight * 10));
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 5),
                        BlockWall.BlockWallHeight * 11));
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 16),
                        BlockWall.BlockWallHeight * 11));
            } else {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 8),
                        BlockWall.BlockWallHeight * 10));

            }
        }
        // 初始化石墙-内部墙-最下一个
        for (int i = 0; i < 5; i++) {
            if (i == 0 || i == 4) {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 9),
                        BlockWall.BlockWallHeight * 13));
            } else {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * (i + 9),
                        BlockWall.BlockWallHeight * 14));
            }
        }
        // 初始化石墙-内部墙-左右两个
        for (int i = 0; i < 2; i++) {
            BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * 3,
                    BlockWall.BlockWallHeight * (i + 7)));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * 19,
                    BlockWall.BlockWallHeight * (i + 7)));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * 6,
                    BlockWall.BlockWallHeight * (i + 16)));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallWidth * 16,
                    BlockWall.BlockWallHeight * (i + 16)));
        }
        // 初始化草丛
        for (int i = 0; i < 5; i++) {
            tree.add(new Tree(Tree.TreeWidth, Tree.TreeHeight * (5 + i)));
            tree.add(new Tree(Tree.TreeWidth * 2, Tree.TreeHeight * (5 + i)));
            tree.add(new Tree(Tree.TreeWidth * 19, Tree.TreeHeight * (5 + i)));
            tree.add(new Tree(Tree.TreeWidth * 20, Tree.TreeHeight * (5 + i)));
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

