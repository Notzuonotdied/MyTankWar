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
class GamePanel extends JPanel implements KeyListener, Runnable {
    // 总成绩
    public static int score = 0;
    // 设置关卡
    public static int level = 0;
    // 定义怪物的数量
    public static int monstersize = 6;
    // 控制界面是否移除的变量
    public static boolean button = false;
    // 控制是否在我的坦克死亡后继续游戏的变量
    public static boolean choice = false;
    public static boolean buttonwin = false;
    public static boolean buttonfail = false;
    // 定义我的坦克类
    static MyTank myTank = null;
    // 定义一个敌人的坦克集合
    static Vector<Monster> monster = new Vector<>();
    // 定义一个爆炸集合
    static Bomb bomb = null;
    static Vector<Bomb> bombs = new Vector<>();
    static Vector<CommentWall> CWalls = new Vector<>();
    // 定义一个石墙集合----这是内部的石墙
    static Vector<BlockWall> BWalls_1 = new Vector<>();
    // 定义我的坦克可以连发的子弹数
    int buttlenumber = 2;
    // 定义一个普通墙集合
    CommentWall CWall = null;
    Vector<BlockWall> BWalls = new Vector<>();
    // 定义一个树木集合
    Tree tr = null;
    Vector<Tree> tree = new Vector<>();
    // 用于判断记录文件是否为空
    FileInputStream fin = null;
    // 定义随机变量
    private Random r = new Random();
    private int movesteps = 0;

    // GamePanel构造函数
    public GamePanel(int flag) {
        int size = 0;
        // 判断记录的文本是否为空
        try {
            // 判读文本是否存在，不存在就新建一个空白文本
            File f = new File("myrecoder.txt");
            if (f.exists()) {
                fin = new FileInputStream("myrecoder.txt");
                // 获取
                size = fin.available();
                // 已经不需要判断了，关闭流
                fin.close();
            } else {
                f.createNewFile();
            }

            if (size != 0 && flag == 1) {
                // 恢复数据
                new Recorder().ReInfo();

            } else {
                // 创建我的坦克,设置坦克的位置
                myTank = new MyTank(600, 500);
                myTank.setDirect(0);
                // 创建怪物的坦克
                for (int i = 0; i < GamePanel.monstersize; i++) {
                    // 定义坦克的位置
                    movesteps = r.nextInt(30) + 35;
                    Monster mon = new Monster((i + 1) * 80, movesteps);
                    Monster.setTankNumber(monstersize);
                    mon.setDirect(2);
                    // 启动敌人坦克
                    Thread mont = new Thread(mon);
                    mont.start();
                    // 将敌人坦克添加到坦克集合中
                    monster.add(mon);
                    // 为敌人添加子弹
                    Bullet bullet = new Bullet(
                            mon.getX() + TankMember.size / 2, mon.getY()
                            + TankMember.size / 2, 2);
                    // 加到子弹集合中
                    mon.bullets.add(bullet);
                    // 启动子弹线程
                    Thread t2 = new Thread(bullet);
                    t2.start();
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
        g.drawImage(Background, 35, -160, MainFrame.screenwidth,
                MainFrame.screenwidth + 80, null);

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
    public void DrawCWall() {
        // 初始化普通墙
        for (int i = 0; i < 15; i++) {
            if (i < 12) {
                CWalls.add(new CommentWall(CommentWall.commentwallwidth
                        * (i + 4), 158));
                CWalls.add(new CommentWall(CommentWall.commentwallwidth
                        * (i + 20), 158));
            }
            CWalls.add(new CommentWall(154, CommentWall.commentwallheight
                    * (i + 8)));
            CWalls.add(new CommentWall(632, CommentWall.commentwallheight
                    * (i + 8)));
        }

        for (int i = 0; i < 3; i++) {
            CWalls.add(new CommentWall(CommentWall.commentwallwidth * (i + 13),
                    215));
            CWalls.add(new CommentWall(CommentWall.commentwallwidth * (i + 13),
                    237));
            CWalls.add(new CommentWall(CommentWall.commentwallwidth * (i + 21),
                    215));
            CWalls.add(new CommentWall(CommentWall.commentwallwidth * (i + 21),
                    237));
        }
    }

    // 画出我的坦克
    public void DrawMyTank(Graphics g) {
        // 画出我自己的坦克
        if (myTank.isLive) {
            myTank.drawTank(myTank.x, myTank.y, g, 0);
            this.repaint();
        }

        // 画出子弹
        for (int i = 0; i < myTank.bullets.size(); i++) {
            Bullet bullet = myTank.bullets.get(i);
            if (bullet != null && bullet.isLive == true
                    && myTank.isLive == true
                    && !bullet.BulletComeAcrossCWall(bullet)
                    && !bullet.BulletComeAcrossMonster(bullet)
                    && !bullet.BulletComeAcrossBWall(bullet)) {
                // 画出子弹的轨迹
                bullet.drawBullet(g);
                // 判断是否集中了怪物
                bullet.HitMonster();
            }
            if (bullet.isLive == false) {
                // 如果子弹存在状态为假就移除子弹
                myTank.bullets.remove(bullet);
            }
        }
    }

    // 画出怪物
    public void DrawMonster(Graphics g) {
        for (int i = 0; i < monster.size(); i++) {
            Monster mon = monster.get(i);
            if (mon.isLive) {
                mon.drawTank(mon.getX(), mon.getY(), g, 1);
                this.repaint();
                for (int j = 0; j < mon.bullets.size(); j++) {
                    // 取出一个子弹
                    Bullet bullet = mon.bullets.get(j);
                    if (bullet.isLive && !bullet.BulletComeAcrossCWall(bullet)
                            && !bullet.BulletComeAcrossBWall(bullet)) {
                        // 画出子弹的轨迹
                        bullet.drawBullet(g);
                        // 判断是否击中了我的坦克
                        bullet.HitMyTank();
                    }
                    if (bullet.isLive == false) {
                        mon.bullets.remove(bullet);
                    }
                }
            }
            // 怪物死亡就移除
            if (mon.isLive == false) {
                monster.remove(mon);
                GamePanel.score++;
            }
        }
    }

    // 画出墙,爆炸效果，树木，普通墙
    public void DrawItem(Graphics g) {
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
            CWall = GamePanel.CWalls.get(i);
            // 如果普通墙存在状态为真就画出来
            if (CWall.isLive) {
                CWall.drawCWall(g);
            } else {
                GamePanel.CWalls.remove(CWall);
            }
        }

        // 画出石墙
        // 外围石墙
        for (int i = 0; i < this.BWalls.size(); i++) {
            this.BWalls.get(i).drawBWall(g);
        }
        // 内部石墙
        for (int i = 0; i < GamePanel.BWalls_1.size(); i++) {
            GamePanel.BWalls_1.get(i).drawBWall(g);
        }

        // 画出树木
        for (int i = 0; i < this.tree.size(); i++) {
            this.tree.get(i).drawTree(g);
        }
    }

    // 信息显示
    public void DrawInfo(Graphics g) {
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
            g.setFont(new Font("TimesRoman", Font.BOLD, 60)); // 判断是否赢得比赛--赢了
            g.drawString("You are Winner！ ", 175, 310);
            g.setFont(f);
            // 选择方案
            GamePanel.buttonwin = true;
            if (level == 3) {
                g.drawString("You are Crazy！ ", 175, 370);
            }
        }
        if (myTank.isLive == false) {
            Font f = g.getFont();
            g.setFont(new Font("TimesRoman", Font.BOLD, 60)); // 判断是否赢得比赛--输了
            g.drawString("You are lost！ ", 180, 310);
            g.setFont(f);
            // 选择方案

            GamePanel.buttonfail = true;
        }
    }

    // 初始化工作
    public void Initialization() {
        // 初始化除了普通墙外的所有墙体--注：其他的墙体都设置为无敌了，不可攻击
        // 初始化石墙-外部围墙
        for (int i = 0; i < 23; i++) {
            BWalls.add(new BlockWall(BlockWall.BlockWallwidth * i, 0));
            BWalls.add(new BlockWall(BlockWall.BlockWallwidth * i,
                    MainFrame.screenheight - BlockWall.BlockWallheight));
            BWalls.add(new BlockWall(0, BlockWall.BlockWallheight * i));
            BWalls.add(new BlockWall(MainFrame.screenwidth
                    - BlockWall.BlockWallwidth, BlockWall.BlockWallheight * i));
        }
        // 初始化石墙-内部围墙
        for (int i = 0; i < 4; i++) {
            BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 6),
                    BlockWall.BlockWallheight * 5));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 13),
                    BlockWall.BlockWallheight * 5));
        }
        // 初始化石墙-内部墙-上两个
        for (int i = 0; i < 5; i++) {
            if (i < 2) {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 5),
                        BlockWall.BlockWallheight * 10));
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 16),
                        BlockWall.BlockWallheight * 10));
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 5),
                        BlockWall.BlockWallheight * 11));
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 16),
                        BlockWall.BlockWallheight * 11));
            } else {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 8),
                        BlockWall.BlockWallheight * 10));

            }
        }
        // 初始化石墙-内部墙-最下一个
        for (int i = 0; i < 5; i++) {
            if (i == 0 || i == 4) {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 9),
                        BlockWall.BlockWallheight * 13));
            } else {
                BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * (i + 9),
                        BlockWall.BlockWallheight * 14));
            }
        }
        // 初始化石墙-内部墙-左右两个
        for (int i = 0; i < 2; i++) {
            BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * 3,
                    BlockWall.BlockWallheight * (i + 7)));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * 19,
                    BlockWall.BlockWallheight * (i + 7)));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * 6,
                    BlockWall.BlockWallheight * (i + 16)));
            BWalls_1.add(new BlockWall(BlockWall.BlockWallwidth * 16,
                    BlockWall.BlockWallheight * (i + 16)));
        }
        // 初始化草丛
        for (int i = 0; i < 5; i++) {
            tree.add(new Tree(Tree.Treewidth * 1, Tree.Treeheight * (5 + i)));
            tree.add(new Tree(Tree.Treewidth * 2, Tree.Treeheight * (5 + i)));
            tree.add(new Tree(Tree.Treewidth * 19, Tree.Treeheight * (5 + i)));
            tree.add(new Tree(Tree.Treewidth * 20, Tree.Treeheight * (5 + i)));
        }
    }

    // 响应键盘
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            myTank.setDirect(2);
            this.repaint();
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            // 设置我的坦克的方向
            myTank.setDirect(0);
            this.repaint();
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            myTank.setDirect(3);
            this.repaint();
            myTank.move();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            myTank.setDirect(1);
            this.repaint();
            myTank.move();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (myTank.bullets.size() < buttlenumber) {
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
                Thread.sleep(66);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }
}

@SuppressWarnings("serial")
// 开始面板
class GameStartPanel extends JPanel implements KeyListener {

    // 返回值
    public static boolean button0 = false;
    public static boolean button1 = false;
    // 图片类
    public int flag = 1;
    // 初始化图片坦克的图片
    Image[] tankImages = new Image[]{
            Toolkit.getDefaultToolkit().getImage(
                    TankMember.class.getResource("/images/1.gif")),

            Toolkit.getDefaultToolkit().getImage(
                    TankMember.class.getResource("/images/m0.gif")),};
    // 设置开始游戏继续游戏的位置
    private int Infox = 320;
    private int Infoy = 400;
    private int x = Infox - TankMember.size / 2 * 3 - 8;
    private int y = Infoy - 30;

    public void paint(Graphics g) {
        super.paint(g);
        // 画出信息代码部分
        DrawInfo(g);
    }

    // 画出信息
    public void DrawInfo(Graphics g) {

        g.fillRect(0, 0, MainFrame.screenwidth + 10, MainFrame.screenheight);
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

        g.drawString("开始游戏", Infox, Infoy);
        g.drawString("继续游戏", Infox, Infoy + 40);
        // 画出坦克
        g.drawImage(tankImages[this.flag], this.x, this.y, null);
        this.repaint();

    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_DOWN
                || e.getKeyCode() == KeyEvent.VK_UP) {
            if (flag == 1) {
                this.x = Infox - TankMember.size / 2 * 3;
                this.y = Infoy + 15;
                this.flag = 0;
            } else {
                this.x = Infox - TankMember.size / 2 * 3 - 8;
                this.y = Infoy - 30;
                this.flag = 1;
            }
            this.repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (this.flag == 0) {
                GameStartPanel.button0 = true;
            } else {
                GameStartPanel.button1 = true;
            }

            // 启动声音
            Audio audio = new Audio("StartGamePanel.wav");
            audio.start();
        }
    }

    public void keyReleased(KeyEvent e) {

    }

}

