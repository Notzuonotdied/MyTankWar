import java.awt.*;
import java.util.Random;
import java.util.Vector;

// 主类-坦克类
abstract class TankMember {
    // 定义坦克的宽度和长度
    public final static int size = 35;
    // 初始化坦克图片
    private static Image[] tankImages = null;// 定义为全局静态变量

    static {
        tankImages = new Image[]{
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/1.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/2.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/3.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/4.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m0.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m1.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m2.gif")),
                Toolkit.getDefaultToolkit().getImage(
                        TankMember.class.getResource("/images/m3.gif")),};
    }

    // 定义我的坦克的生命状态
    public boolean isLive = true;
    // 定义坐标（x,y）
    public int x = 0;
    public int y = 0;
    // 定义坦克的速度
    public int speed = 5;
    // 定义坦克的方向
    // 说明：0为方向上，1为方向右，2为方向下，3为方向左
    public int direct = 0;
    // 定义一个类型：0.为我的坦克，1.为怪物
    int type;
    // 定义子弹集合
    Bullet bullet = null;
    Vector<Bullet> bullets = new Vector<Bullet>();
    // 定义爆炸集合
    Bomb bomb = null;
    Vector<Bomb> bombs = new Vector<Bomb>();
    Graphics g = null;
    private int oldX, oldY;

    // 以下为成员变量的构造函数
    public TankMember(int x, int y) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
    }

    // 开火的能力
    public void ShotEnemy() {
        if (GamePanel.myTank.isLive) {
            // 启动声音
            Audio audio = new Audio("Shot.wav");
            audio.start();
        }
        switch (this.direct) {
            case 0:// 上
                bullet = new Bullet(x + (size - 8) / 2, y, 0);
                bullets.add(bullet);
                break;
            case 1:// 右
                bullet = new Bullet(x + size / 2, y + (size - 6) / 2, 1);
                bullets.add(bullet);
                break;
            case 2:// 下
                bullet = new Bullet(x + (size - 10) / 2, y + size / 2, 2);
                bullets.add(bullet);
                break;
            case 3:// 左
                bullet = new Bullet(x, y + (size - 6) / 2, 3);
                bullets.add(bullet);
                break;
        }
        // 启动子弹线程
        Thread t = new Thread(bullet);
        t.start();
    }

    // 画出坦克的函数
    public void drawTank(int x, int y, Graphics g, int type) {
        switch (direct) {
            case 0:
                if (type == 1) {
                    g.drawImage(tankImages[0], x, y, null);
                } else {
                    g.drawImage(tankImages[4], x - 7, y - 7, null);
                }
                break;
            case 1:
                if (type == 1) {
                    g.drawImage(tankImages[1], x, y, null);
                } else {
                    g.drawImage(tankImages[5], x - 7, y - 7, null);
                }
                break;
            case 2:
                if (type == 1) {
                    g.drawImage(tankImages[2], x, y, null);
                } else {
                    g.drawImage(tankImages[6], x - 7, y - 7, null);
                }
                break;
            case 3:
                if (type == 1) {
                    g.drawImage(tankImages[3], x, y, null);
                } else {
                    g.drawImage(tankImages[7], x - 7, y - 7, null);
                }
                break;
        }
    }

    // 定义我的坦克的移动函数
    public abstract void move();

    // 一旦与其他坦克相遇就转向
    public int DictionDirection(int direct) {
        switch (type) {
            case 0:
                while (direct == 0) {
                    this.direct = (int) (Math.random() * 4);
                }
                break;
            case 1:
                while (direct == 1) {
                    this.direct = (int) (Math.random() * 4);
                }

                break;
            case 2:
                while (direct == 2) {
                    this.direct = (int) (Math.random() * 4);
                }
                break;
            case 3:
                while (direct == 3) {
                    this.direct = (int) (Math.random() * 4);
                }
                break;
        }
        return direct;
    }

    // 判断是否越界
    public boolean isTouchBorder() {
        if (x < BlockWall.BlockWallwidth) {
            x = BlockWall.BlockWallwidth;
            return true;
        }
        if (y < BlockWall.BlockWallheight) {
            y = BlockWall.BlockWallheight;
            return true;
        }
        if (x + size > MainFrame.rangx) {
            x = MainFrame.rangx - size;
            return true;
        }
        if (y + size > MainFrame.rangy) {
            y = MainFrame.rangy - size;
            return true;
        }
        return false;
    }

    // 判断是否与普通墙相撞
    public abstract boolean isTouchCWall();

    // 判断是否与石墙相撞
    public abstract boolean isTouchBWall();

    // 判断是否接触
    public abstract boolean isTouchOtherTank(int direct);

    // 调用Rectangle函数
    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, size, size);
    }

    // 保存旧的位置,保证出现错误的移动可以恢复到原先的位置
    public void settoOldDirect() {
        this.oldX = x;
        this.oldY = y;
    }

    // 在出现错误的移动后，改变现有位置为保存的旧的位置
    public void changtoOldDirect() {
        x = oldX;
        y = oldY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(Vector<Bullet> bullets) {
        this.bullets = bullets;
    }

}

// 我的坦克类
class MyTank extends TankMember {
    // 定义坦克的生命值
    public static int blood = 200;
    // 定义坦克的速度
    public int speed = 7;

    public MyTank(int x, int y) {
        super(x, y);
    }

    // 防止重叠
    public boolean isTouchOtherTank(int direct) {
        switch (this.direct) {
            case 0:
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    Monster mon = GamePanel.monster.get(i);
                    if (mon.direct == 0 || mon.direct == 2) {
                        // 左点
                        if (this.x >= mon.x && this.x <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                        if (this.x + size + 1 >= mon.x
                                && this.x + size + 1 <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                    }
                    if (mon.direct == 3 || mon.direct == 1) {
                        if (this.x >= mon.x && this.x <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                        if (this.x + size + 1 >= mon.x
                                && this.x + size + 1 <= mon.x + size + 1
                                && this.y >= mon.y && this.y <= mon.y + size + 1) {
                            return true;
                        }
                    }
                }
                break;
            case 1:
                // 坦克向右
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果敌人的方向是向下或者向上
                    if (mon.direct == 0 || mon.direct == 2) {
                        // 上点
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        // 下点
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                    if (mon.direct == 3 || mon.direct == 1) {
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                }
                break;
            case 2:
                // 坦克向下
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果敌人的方向是向下或者向上
                    if (mon.direct == 0 || mon.direct == 2) {
                        // 我的左点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                        // 我的右点
                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                    if (mon.direct == 3 || mon.direct == 1) {
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }

                        if (this.x + size >= mon.x && this.x + size <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                }

                break;
            case 3:
                // 向左
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果敌人的方向是向下或者向上
                    if (mon.direct == 0 || mon.direct == 2) {
                        // 我的上一点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        // 下一点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                    if (mon.direct == 3 || mon.direct == 1) {
                        // 上一点
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y >= mon.y && this.y <= mon.y + size) {
                            return true;
                        }
                        if (this.x >= mon.x && this.x <= mon.x + size
                                && this.y + size >= mon.y
                                && this.y + size <= mon.y + size) {
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    // 定义我的坦克的移动函数
    public void move() {
        // 保存旧的位置
        settoOldDirect();
        switch (direct) { // 选择移动方向
            case 0:
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    this.y -= speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.changtoOldDirect();
                    }
                }
                break;
            case 1:
                this.settoOldDirect();
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    this.x += speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.changtoOldDirect();
                    }
                }
                break;
            case 2:
                this.settoOldDirect();
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    this.y += speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.changtoOldDirect();
                    }
                }
                break;
            case 3:
                this.settoOldDirect();
                if (!isTouchOtherTank(direct) && !isTouchBorder()
                        && !isTouchCWall() && !isTouchBWall()) {
                    x -= speed;
                    if (isTouchOtherTank(direct) || isTouchBorder()
                            || isTouchCWall() || isTouchBWall()) {
                        this.changtoOldDirect();
                    }
                }
                break;
        }

    }

    public boolean isTouchCWall() {
        for (int i = 0; i < GamePanel.CWalls.size(); i++) {
            CommentWall CWall = GamePanel.CWalls.get(i);
            if (GamePanel.myTank.getRect().intersects(CWall.getRect())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouchBWall() {
        for (int i = 0; i < GamePanel.BWalls_1.size(); i++) {
            BlockWall BWall = GamePanel.BWalls_1.get(i);
            if (GamePanel.myTank.getRect().intersects(BWall.getRect())) {
                return true;
            }
        }
        return false;
    }
}

// 怪物类
// 由于怪物是多个的，且要求互不影响，所以要做成多线程的
class Monster extends TankMember implements Runnable {
    // 定义坦克的生命值
    public static int blood = 50;
    // 定义怪物连发子弹的数量
    static int bulletsize = 2;
    // 定义怪物的数量
    private static int TankNumber = 0;
    // 定义随机变量
    private static Random r = new Random();
    // 定义怪物的停顿时间
    private final int WaitingTime = 25;
    // 定义让坦克随机产生一个随机的新的方向的等待时间
    private final int WaitingforRandom = 30;
    // 定义坦克类型
    int type = 1;
    private int movesteps = r.nextInt(30) + 15;

    // 调用父类的构造函数进行初始化
    public Monster(int x, int y) {
        super(x, y);
    }

    // 以下为成员变量的构造函数
    public static int getTankNumber() {
        return TankNumber;
    }

    public static void setTankNumber(int tankNumber) {
        TankNumber = tankNumber;
    }

    // 定义我的坦克的移动函数
    public void move() {
        // 保存旧的位置,保证出现错误的移动可以恢复到原先的位置
        settoOldDirect();

        switch (direct) { // 选择移动方向
            case 0:
                y -= speed;
                break;
            case 1:
                x += speed;
                break;
            case 2:
                y += speed;
                break;
            case 3:
                x -= speed;
                break;
        }
    }

    // 防止重叠
    // public boolean isTouchOtherTank(int direct) {
    // for (int i = 0; i < GamePanel.monster.size(); i++) {
    // Monster mon = GamePanel.monster.get(i);
    // if (this != mon) {
    // if (mon.getRect().intersects(this.getRect())) {
    // return true;
    // }
    // }
    // }
    // return false;
    // }
    //
    // public boolean isTouchMyTank(Monster mon) {
    // if (mon.getRect().intersects(GamePanel.myTank.getRect())) {
    // return true;
    // }
    // return false;
    // }
    public boolean isTouchOtherTank(int direct) {
        switch (this.direct) {
            case 0:
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    Monster mon = GamePanel.monster.get(i);
                    if (this != mon) {
                        if (mon.direct == 0 || mon.direct == 2) {
                            // 左点
                            if (this.x >= mon.x && this.x <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= mon.x
                                    && this.x + size + 1 <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                        }
                        if (mon.direct == 3 || mon.direct == 1) {
                            if (this.x >= mon.x && this.x <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= mon.x
                                    && this.x + size + 1 <= mon.x + size + 1
                                    && this.y >= mon.y
                                    && this.y <= mon.y + size + 1) {
                                return true;
                            }
                        }
                    }
                    if (this.type == 1) {
                        if (GamePanel.myTank.direct == 0
                                || GamePanel.myTank.direct == 2) {
                            // 左点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= GamePanel.myTank.x
                                    && this.x + size + 1 <= GamePanel.myTank.x
                                    + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == 3
                                || GamePanel.myTank.direct == 1) {
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                            if (this.x + size + 1 >= GamePanel.myTank.x
                                    && this.x + size + 1 <= GamePanel.myTank.x
                                    + size + 1
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size + 1) {
                                return true;
                            }
                        }
                    }
                }
            case 1:
                // 坦克向右
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果不是自己
                    if (mon != this) {
                        // 如果敌人的方向是向下或者向上
                        if (mon.direct == 0 || mon.direct == 2) {
                            // 上点
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y >= mon.y && this.y <= mon.y + size) {
                                return true;
                            }
                            // 下点
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                        if (mon.direct == 3 || mon.direct == 1) {
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y >= mon.y && this.y <= mon.y + size) {
                                return true;
                            }
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                    }
                    if (this.type == 1) {
                        // 如果敌人的方向是向下或者向上
                        if (GamePanel.myTank.direct == 0
                                || GamePanel.myTank.direct == 2) {
                            // 上点
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            // 下点
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == 3
                                || GamePanel.myTank.direct == 1) {
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 2:
                // 坦克向下
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果不是自己
                    if (mon != this) {
                        // 如果敌人的方向是向下或者向上
                        if (mon.direct == 0 || mon.direct == 2) {
                            // 我的左点
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                            // 我的右点
                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                        if (mon.direct == 3 || mon.direct == 1) {
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }

                            if (this.x + size >= mon.x
                                    && this.x + size <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                    }
                    if (this.type == 1) {
                        // 如果敌人的方向是向下或者向上
                        if (GamePanel.myTank.direct == 0
                                || GamePanel.myTank.direct == 2) {
                            // 我的左点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            // 我的右点
                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == 3
                                || GamePanel.myTank.direct == 1) {
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }

                            if (this.x + size >= GamePanel.myTank.x
                                    && this.x + size <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 3:
                // 向左
                // 取出所有的敌人坦克
                for (int i = 0; i < GamePanel.monster.size(); i++) {
                    // 取出第一个坦克
                    Monster mon = GamePanel.monster.get(i);
                    // 如果不是自己
                    if (mon != this) {
                        // 如果敌人的方向是向下或者向上
                        if (mon.direct == 0 || mon.direct == 2) {
                            // 我的上一点
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y >= mon.y && this.y <= mon.y + size) {
                                return true;
                            }
                            // 下一点
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                        if (mon.direct == 3 || mon.direct == 1) {
                            // 上一点
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y >= mon.y && this.y <= mon.y + size) {
                                return true;
                            }
                            if (this.x >= mon.x && this.x <= mon.x + size
                                    && this.y + size >= mon.y
                                    && this.y + size <= mon.y + size) {
                                return true;
                            }
                        }
                    }
                    if (this.type == 1) {
                        // 如果敌人的方向是向下或者向上
                        if (GamePanel.myTank.direct == 0
                                || GamePanel.myTank.direct == 2) {
                            // 我的上一点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            // 下一点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                        if (GamePanel.myTank.direct == 3
                                || GamePanel.myTank.direct == 1) {
                            // 上一点
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y >= GamePanel.myTank.y
                                    && this.y <= GamePanel.myTank.y + size) {
                                return true;
                            }
                            if (this.x >= GamePanel.myTank.x
                                    && this.x <= GamePanel.myTank.x + size
                                    && this.y + size >= GamePanel.myTank.y
                                    && this.y + size <= GamePanel.myTank.y + size) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    // 禁止几秒
    private void StopSeconds() {
        int olddirect = this.direct;
        int waitingtime = r.nextInt(6);
        try {
            Thread.sleep(waitingtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        direct = olddirect;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (this.direct) {
                case 0:
                    // 说明坦克正在向上移动
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.changtoOldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    // 向右
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.changtoOldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    // 向下
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.changtoOldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    // 向左
                    for (int i = 0; i < movesteps; i++) {
                        // 判断坦克是否遇到障碍物，遇到就停止
                        if (!isTouchOtherTank(direct) && !isTouchBWall()
                                && !isTouchCWall() && !isTouchBorder()) {
                            this.move();
                            // 判断坦克行走后是否遇到障碍物，遇到就停止并还原移动
                            if (isTouchOtherTank(direct) || isTouchBWall()
                                    || isTouchCWall() || isTouchBorder()) {
                                this.changtoOldDirect();
                                StopSeconds();
                            }
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(WaitingTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            // 添加子弹
            AddBullets();

            try {
                // 让坦克随机产生一个随机的新的方向
                Thread.sleep(WaitingforRandom);
                this.direct = (int) (Math.random() * 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 判断敌人坦克是否死亡
            if (this.isLive == false) {
                // 让坦克死亡后退出线程
                break;
            }
        }
    }

    private void AddBullets() {
        // 判断是否需要给坦克加入新的子弹
        if (true) {
            if (isLive) {
                if (bullets.size() < bulletsize) {
                    if (GamePanel.myTank.isLive && bullets.size() > 2) {
                        // 启动声音
                        Audio audio = new Audio("Shot.wav");
                        audio.start();
                    }
                    // 没有子弹，添加
                    Bullet bullet = null;
                    switch (direct) {
                        case 0:// 上
                            bullet = new Bullet(x + (size - 8) / 2, y, 0);
                            this.bullets.add(bullet);
                            break;
                        case 1:// 右
                            bullet = new Bullet(x + size / 2, y + (size - 6) / 2, 1);
                            this.bullets.add(bullet);
                            break;
                        case 2:// 下
                            bullet = new Bullet(x + (size - 10) / 2, y + size / 2,
                                    2);
                            this.bullets.add(bullet);
                            break;
                        case 3:// 左
                            bullet = new Bullet(x, y + (size - 6) / 2, 3);
                            this.bullets.add(bullet);
                            break;
                    }
                    // 启动子弹线程
                    Thread t = new Thread(bullet);
                    t.start();
                }
            }
        }
    }

    public boolean isTouchCWall() {
        for (int i = 0; i < GamePanel.CWalls.size(); i++) {
            CommentWall CWall = GamePanel.CWalls.get(i);
            for (int j = 0; j < GamePanel.monster.size(); j++) {
                Monster mon = GamePanel.monster.get(j);
                if (CWall.getRect().intersects(mon.getRect())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTouchBWall() {
        for (int i = 0; i < GamePanel.BWalls_1.size(); i++) {
            BlockWall BWall = GamePanel.BWalls_1.get(i);
            for (int j = 0; j < GamePanel.monster.size(); j++) {
                Monster mon = GamePanel.monster.get(j);
                if (BWall.getRect().intersects(mon.getRect())) {
                    return true;
                }
            }
        }
        return false;
    }

}