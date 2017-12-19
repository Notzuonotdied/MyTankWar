import java.awt.*;
import java.util.Vector;

public class Bullet implements Runnable {
    // 定义每一颗子弹的伤害值
    public static int damage = 25;
    // 定义子弹的尺寸
    static int bulletwidth = 12;
    static int bulletheight = 12;
    // 子弹图片初始化
    // 定义为全局静态变量
    private static Image[] BulletImages = null;

    static {
        BulletImages = new Image[]{
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bullet1.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bullet2.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bullet3.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        Bullet.class.getResource("/images/bullet4.gif")),};
    }

    // 定义坐标
    int x;
    int y;
    // 定义爆炸集合
    Bomb bomb = null;
    Vector<Bomb> bombs = new Vector<>();
    // 定义子弹的速度
    int speed = 16;
    // 定义子弹的存在状态
    boolean isLive = true;
    // 定义方向
    int direct = 0;
    // 定义一个子弹集合
    Vector<Bullet> bullets = new Vector<>();
    Graphics g = null;

    // 构造函数
    public Bullet(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    // 画出子弹
    public void drawBullet(Graphics g) {
        switch (direct) {
            case 0:
                g.drawImage(BulletImages[0], x, y, null);
                break;
            case 1:
                g.drawImage(BulletImages[1], x, y, null);
                break;
            case 2:
                g.drawImage(BulletImages[2], x, y, null);
                break;
            case 3:
                g.drawImage(BulletImages[3], x, y, null);
                break;
        }
    }

    // 判断怪物和我的子弹与普通墙相遇的时候--击中普通墙，则普通墙消失，子弹也消失
    public boolean BulletComeAcrossCWall(Bullet bullet) {
        for (int i = 0; i < GamePanel.CWalls.size(); i++) {
            CommentWall CWall = GamePanel.CWalls.get(i);
            if (CWall.getRect().intersects(bullet.bulletgetRect())) {
                bullet.isLive = false;
                CWall.isLive = false;
                GamePanel.CWalls.remove(CWall);
                return true;
            }
        }
        return false;
    }

    // 判断怪物和我的子弹与石墙相遇的时候--子弹消失
    public boolean BulletComeAcrossBWall(Bullet bullet) {
        for (int i = 0; i < GamePanel.BWalls_1.size(); i++) {
            BlockWall BWall = GamePanel.BWalls_1.get(i);
            if (BWall.getRect().intersects(bullet.bulletgetRect())) {
                bullet.isLive = false;
                return true;
            }
        }
        return false;
    }

    // 判断我的坦克的子弹和怪物的子弹相遇的情况，子弹相遇后抵消，都消失
    public boolean BulletComeAcrossMonster(Bullet bullet) {
        for (int i = 0; i < GamePanel.monster.size(); i++) {
            Monster mon = GamePanel.monster.get(i);
            for (int j = 0; j < mon.bullets.size(); j++) {
                Bullet b = mon.bullets.get(j);
                if (b.bulletgetRect().intersects(bullet.bulletgetRect())) {
                    // 两者的存在状态都为死亡
                    bullet.isLive = false;
                    mon.bullets.remove(b);
                    return true;
                }
            }
        }
        return false;
    }

    // 判断怪物是否击中了我
    public void HitMyTank() {
        // 取出每一只怪物
        for (int i = 0; i < GamePanel.monster.size(); i++) {
            // 取出怪物
            Monster mon = GamePanel.monster.get(i);
            bullets = mon.getBullets();
            // 取出怪物的每一个子弹
            for (int j = 0; j < bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                if (GamePanel.myTank.isLive) {
                    // 调用判断是否相交的函数
                    HitTank(bullet, GamePanel.myTank);
                }
            }
        }
    }

    // 判断我的坦克的子弹是否击中了怪物
    public void HitMonster() {
        // 判断是否击中了怪物
        for (int i = 0; i < GamePanel.myTank.bullets.size(); i++) {
            // 取出子弹
            Bullet bullet = GamePanel.myTank.bullets.get(i);
            if (bullet.isLive) {
                // 取出每只怪物，进行匹配
                for (int j = 0; j < GamePanel.monster.size(); j++) {
                    // 取出怪物
                    Monster mon = GamePanel.monster.get(j);
                    if (mon.isLive) {
                        // 调用判断是否相交的函数
                        HitTank(bullet, mon);
                    }
                }
            }
        }
    }

    // 判断子弹是否击中了目标--我的坦克或者是怪物
    public boolean HitTank(Bullet bullet, TankMember tank) {
        if (bullet.bulletgetRect().intersects(tank.getRect())) {
            // 两者的存在状态都为死亡
            bullet.isLive = false;
            tank.isLive = false;
            // 把子弹从子弹集合中去除
            tank.bullets.remove(bullet);
            // 新建爆炸效果
            GamePanel.bomb = new Bomb(tank.getX(), tank.getY());
            GamePanel.bombs.add(GamePanel.bomb);
            try {
                Thread.sleep(50);
                // 启动声音
                Audio audio = new Audio("Explosion.wav");
                audio.start();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    // run函数
    public void run() {
        while (isLive) {
            switch (direct) {
                case 0:
                    // 上
                    y -= speed;
                    break;
                case 1:
                    // 右
                    x += speed;
                    break;
                case 2:
                    // 下
                    y += speed;
                    break;
                case 3:
                    // 左
                    x -= speed;
                    break;
            }
            try {
                Thread.sleep(66);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 子弹何时死亡
            if (x < 0 || x > MainFrame.rangx || y < 0 || y > MainFrame.rangy) {
                this.isLive = false;
                break;
            }

        }
    }

    // 调用坦克类的Rectangle函数
    public Rectangle getRect() {
        return new Rectangle(x, y, TankMember.size, TankMember.size);
    }

    // 调用子弹类的Rectangle函数
    public Rectangle bulletgetRect() {
        return new Rectangle(x, y, bulletwidth, bulletheight);
    }

}