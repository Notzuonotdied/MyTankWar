import java.io.*;

public class Recorder {

    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    private static FileReader fr = null;
    private static BufferedReader br = null;
    // 临时起保存作用的字符串
    private static String bulletscoordinate = "";
    // 临时变量
    private static Monster mon = null;
    private static CommentWall cwall = null;

    public void SaveCWallInfo() {
        try {
            // 创建
            fw = new FileWriter("myrecoder.txt", true);
            bw = new BufferedWriter(fw);

            // 保存所有活着的怪物的坐标和方向
            for (int i = 0; i < GamePanel.CWalls.size(); i++) {
                cwall = GamePanel.CWalls.get(i);
                if (cwall.isLive) {
                    // 把活的怪物的数据读取出来
                    String recoder = cwall.x + " " + cwall.y;
                    // 把活的怪物的数据写入到文本中
                    // 2作为标识符，作为普通墙的标识符
                    bw.write("2" + " " + recoder + "\r\n");
                }
            }
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        } finally {
            // 关闭流，先开后关，后开先关
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                // 捕获错误并输出
                e.printStackTrace();
            }
        }
    }

    public void SaveMyTankInfo() {
        try {
            // 创建
            fw = new FileWriter("myrecoder.txt", true);
            bw = new BufferedWriter(fw);
            bulletscoordinate = "";

            // 保存我的坦克的坐标和方向
            if (GamePanel.myTank.isLive == true) {
                for (int i = 0; i < GamePanel.myTank.bullets.size(); i++) {
                    Bullet bullet = GamePanel.myTank.bullets.get(i);
                    if (bullet.isLive == true) {
                        // 如果子弹的存在状态是真的话，就保存子弹的数据累加到字符串中
                        bulletscoordinate += bullet.x + " " + bullet.y + " "
                                + bullet.direct + " ";
                    }
                }
                // 把我的坦克的数据读取出来
                String recoder = GamePanel.myTank.x + " " + GamePanel.myTank.y
                        + " " + GamePanel.myTank.direct + " "
                        + bulletscoordinate;
                // 把我的坦克的数据写入到文本中
                // 0作为标识符，来区分我的坦克和怪物的数据
                bw.write("0" + " " + recoder + "\r\n");
            }
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        } finally {
            // 关闭流，先开后关，后开先关
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                // 捕获错误并输出
                e.printStackTrace();
            }
        }
    }

    public void SaveMonsterInfo() {
        try {
            // 创建
            fw = new FileWriter("myrecoder.txt");
            bw = new BufferedWriter(fw);

            // 保存所有活着的怪物的坐标和方向
            for (int i = 0; i < GamePanel.monster.size(); i++) {
                Monster mon = GamePanel.monster.get(i);
                if (mon.isLive == true) {
                    for (int j = 0; j < mon.bullets.size(); j++) {
                        Bullet bullet = mon.bullets.get(j);
                        if (bullet.isLive == true) {
                            // 如果子弹的存在状态是真的话，就保存子弹的数据累加到字符串中
                            bulletscoordinate += bullet.x + " " + bullet.y
                                    + " " + bullet.direct + " ";
                        }
                    }
                    // 把活的怪物的数据读取出来
                    String recoder = mon.x + " " + mon.y + " " + mon.direct
                            + " " + bulletscoordinate;
                    // 把活的怪物的数据写入到文本中
                    // 1作为标识符，来区分我的坦克和怪物的数据
                    bw.write("1" + " " + recoder + "\r\n");
                    // 清空字符串
                    bulletscoordinate = "";
                }
            }
        } catch (Exception e) {
            // 将错误输出
            e.printStackTrace();
        } finally {
            // 关闭流，先开后关，后开先关
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                // 捕获错误并输出
                e.printStackTrace();
            }
        }
    }

    // 从记录文本文件中恢复数据
    // 原理：将数据从文本中一行行读取出来后对游戏面板中的怪物集合和我的坦克进行初始化
    // 也就是说省略了实例化创建我的坦克和怪物坦克的过程。
    public void ReInfo() {
        try {
            fr = new FileReader("myrecoder.txt");
            br = new BufferedReader(fr);
            String Info = "";

            Bullet bullet = null;

            // 临时变量
            int tempx = 0;
            int tempy = 0;
            int tempd = 0;

            while ((Info = br.readLine()) != null) {

                String[] RecoveryInfo = Info.split(" ");
                // 类型转换，作为判断标识符
                int n = Integer.parseInt(RecoveryInfo[0]);

                // 如果第一个标识符是1，数据就是怪物的
                if (n == 1) {
                    // 类型转换
                    tempx = Integer.parseInt(RecoveryInfo[1]);
                    tempy = Integer.parseInt(RecoveryInfo[2]);
                    tempd = Integer.parseInt(RecoveryInfo[3]);

                    // 恢复并进行初始化
                    mon = new Monster(tempx, tempy);
                    mon.direct = tempd;
                    mon.isLive = true;
                    GamePanel.monster.add(mon);
                    // 启动敌人坦克
                    Thread mont = new Thread(mon);
                    mont.start();

                    // 子弹的数据
                    int num = (RecoveryInfo.length - 1) / 3 - 1;
                    if (RecoveryInfo.length > 4) {
                        for (int i = 0; i < num; i++) {
                            if (i < num) {
                                // 类型转换
                                tempx = Integer
                                        .parseInt(RecoveryInfo[3 * i + 4]);
                                tempy = Integer
                                        .parseInt(RecoveryInfo[3 * i + 5]);
                                tempd = Integer
                                        .parseInt(RecoveryInfo[3 * i + 6]);

                                // 恢复并进行初始化
                                bullet = new Bullet(tempx, tempy, tempd);
                                mon.bullets.add(bullet);

                                // 启动子弹线程
                                Thread t = new Thread(bullet);
                                t.start();
                            }
                        }
                    }
                }

                // 如果第一个标识符是0，数据就是我的坦克的
                if (n == 0) {
                    // 类型转换
                    tempx = Integer.parseInt(RecoveryInfo[1]);
                    tempy = Integer.parseInt(RecoveryInfo[2]);
                    tempd = Integer.parseInt(RecoveryInfo[3]);

                    // 恢复并进行初始化
                    GamePanel.myTank = new MyTank(tempx, tempy);
                    GamePanel.myTank.direct = tempd;
                    GamePanel.myTank.isLive = true;

                    // 子弹的数据
                    int num = (RecoveryInfo.length - 1) / 3 - 1;
                    if (RecoveryInfo.length > 4) {
                        for (int i = 0; i < num; i++) {
                            if (i < num) {
                                // 类型转换
                                tempx = Integer
                                        .parseInt(RecoveryInfo[3 * i + 4]);
                                tempy = Integer
                                        .parseInt(RecoveryInfo[3 * i + 5]);
                                tempd = Integer
                                        .parseInt(RecoveryInfo[3 * i + 6]);

                                // 恢复并进行初始化
                                bullet = new Bullet(tempx, tempy, tempd);
                                GamePanel.myTank.bullets.add(bullet);
                                Thread t = new Thread(bullet);
                                t.start();
                            }
                        }
                    }
                }

                // 如果第一个标识符是2，数据就是普通墙的
                if (n == 2) {
                    // 类型转换
                    tempx = Integer.parseInt(RecoveryInfo[1]);
                    tempy = Integer.parseInt(RecoveryInfo[2]);

                    // 恢复并进行初始化
                    cwall = new CommentWall(tempx, tempy);
                    cwall.isLive = true;
                    GamePanel.CWalls.add(cwall);
                }
            }
        } catch (Exception e) {
            // 捕获错误信息
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (Exception e2) {
                // 捕获错误
                e2.printStackTrace();
            }
        }
    }

}