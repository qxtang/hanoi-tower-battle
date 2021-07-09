package com.tqx.ui;

import com.tqx.game.GamePanel;
import com.tqx.game.MainGame;
import com.tqx.game.SmallGame;
import com.tqx.socket.GameUDPSocket;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

public class AppMain extends JFrame {

    private JButton btnJoin;
    private JButton btnStart;
    private JButton btnExit;

    private JPanel contentPane;
    private JLabel lblShowTime;
    private JLabel lblMsg;

    private JRadioButton rbtnSingle;
    private JRadioButton rbtnBattle;

    private JComboBox comboBoxIPs;
    private JSpinner spinnerPlateCount;

    private GamePanel mainPane;
    private GamePanel opponentPane;

    // private JPanel mainPane;
    // private JPanel opponentPane;

    private MainGame game;
    private SmallGame oppoGame;

    private GameUDPSocket socket = new GameUDPSocket();

    private boolean isBattle = false;
    private Timer timer;
    private int seconds = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AppMain frame = new AppMain();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AppMain() {
        setTitle("Hanoi Tower One On One");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700 + 400, 400 + 200);
        contentPane = new JPanel();
        setContentPane(contentPane);

        mainPane = new GamePanel();
        mainPane.setBounds(0, 200, 700, 400);

        opponentPane = new GamePanel();
        opponentPane.setBounds(700, 0, 400, 200);

        rbtnSingle = new JRadioButton("单机游戏");
        rbtnSingle.setBounds(27, 21, 121, 23);
        rbtnSingle.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        rbtnSingle.setSelected(true);
        rbtnSingle.getModel().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    /* rbtnSingle */
                    lblMsg.setText("");

                    if (socket != null) {
                        socket.send("exit;");
                    }

                    isBattle = false;
                    comboBoxIPs.setEnabled(false);
                    btnJoin.setEnabled(false);
                    btnStart.setEnabled(true);
                    spinnerPlateCount.setEnabled(true);
                }
            }
        });

        rbtnBattle = new JRadioButton("局域网单挑");
        rbtnBattle.setBounds(27, 62, 121, 23);
        rbtnBattle.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        rbtnBattle.getModel().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    /* rbtnBattle */
                    isBattle = true;
                    comboBoxIPs.setEnabled(true);
                    btnJoin.setEnabled(true);
                    btnStart.setEnabled(false);

                    spinnerPlateCount.setEnabled(true);
                    timer.stop();
                    seconds = 0;

                    lblShowTime.setText("Time: ");
                    btnStart.setText("Start");

                    isOppoWin = false;
                }
            }
        });

        comboBoxIPs = new JComboBox();
        comboBoxIPs.setBounds(154, 64, 219, 21);
        comboBoxIPs.setEnabled(false);
        comboBoxIPs.setModel(new DefaultComboBoxModel(new String[]{"127.0.0.1", "192.168.155.0", "192.168.155.1",
                "192.168.155.2", "192.168.155.3", "192.168.155.4", "192.168.155.5"}));
        comboBoxIPs.setEditable(true);

        btnJoin = new JButton("Join");
        btnJoin.setBounds(563, 62, 127, 23);
        btnJoin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* Join */
//				int localPort = Integer.parseInt(txtLocalPort.getText());
//				int remotePort = Integer.parseInt(txtRemotePort.getText());
                String ip = comboBoxIPs.getSelectedItem().toString();
                socket.setLocalPort(8888);
                socket.setRemotePort(8888);
                socket.setRemoteAddress(ip);

                socket.join();
                socket.send(GameUDPSocket.JOIN + ";");
                lblMsg.setText("等待对方响应...");
                btnJoin.setEnabled(false);
            }
        });
        btnJoin.setEnabled(false);
        btnJoin.setFont(new Font("微软雅黑", Font.BOLD, 15));

        spinnerPlateCount = new JSpinner();
        spinnerPlateCount.setBounds(123, 115, 75, 53);
        spinnerPlateCount.setFont(new Font("微软雅黑", Font.BOLD, 25));
        spinnerPlateCount.setModel(new SpinnerNumberModel(3, 1, 10, 1));
        spinnerPlateCount.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int count = Integer.valueOf(((SpinnerModel) e.getSource()).getValue().toString());
                game.setPlateCount(count);
                game.reset();
            }
        });

        btnStart = new JButton("Start");
        btnStart.setBounds(246, 115, 127, 53);
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* Start */

                if (isBattle) {
                    game.reset();

                    socket.send(GameUDPSocket.START + ";");

                    btnStart.setEnabled(false);

                    spinnerPlateCount.setEnabled(false);
                    seconds = 0;
                    timer.start();

                    lblMsg.setText("游戏开始!!");
                } else {
                    if (timer.isRunning()) {
                        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Replay?")) {
                            game.reset();
//							spinnerPlateCount.setEnabled(true);
//							timer.stop();
                            seconds = 0;

//							btnStart.setText("Start");
                            lblShowTime.setText("Time: ");
                        }

                    } else {
                        game.reset();
                        spinnerPlateCount.setEnabled(false);
                        seconds = 0;
                        timer.start();

                        btnStart.setText("Replay");
                    }
                    lblMsg.setText("");
                }
            }
        });
        btnStart.setFont(new Font("微软雅黑", Font.BOLD, 18));

        JLabel lblLevel = new JLabel("Level:");
        lblLevel.setBounds(27, 118, 86, 53);
        lblLevel.setFont(new Font("微软雅黑", Font.BOLD, 18));

        btnExit = new JButton("Quit");
        btnExit.setBounds(938, 481, 127, 53);
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                socket.send("exit;");
                socket.close();
                System.exit(NORMAL);
            }
        });
        btnExit.setFont(new Font("微软雅黑", Font.BOLD, 18));

        lblShowTime = new JLabel("Time: ");
        lblShowTime.setBounds(738, 241, 86, 80);
        lblShowTime.setFont(new Font("微软雅黑", Font.BOLD, 18));

        lblMsg = new JLabel("");
        lblMsg.setBounds(383, 108, 400, 70);
        lblMsg.setForeground(Color.GREEN);
        lblMsg.setFont(new Font("微软雅黑", Font.BOLD, 20));

//		txtLocalPort = new JTextField();
//		txtLocalPort.setBounds(388, 64, 66, 21);
//		txtLocalPort.setText("8888");
//		txtLocalPort.setColumns(10);
//
//		txtRemotePort = new JTextField();
//		txtRemotePort.setBounds(464, 64, 66, 21);
//		txtRemotePort.setText("8888");
//		txtRemotePort.setColumns(10);

        contentPane.setLayout(null);

        ButtonGroup rbtnGroup = new ButtonGroup();
        rbtnGroup.add(rbtnBattle);
        rbtnGroup.add(rbtnSingle);
        contentPane.add(rbtnSingle);
        contentPane.add(rbtnBattle);
        contentPane.add(comboBoxIPs);
        contentPane.add(lblLevel);
        contentPane.add(spinnerPlateCount);
        contentPane.add(btnStart);
//		contentPane.add(txtLocalPort);
//		contentPane.add(txtRemotePort);
        contentPane.add(btnJoin);
        contentPane.add(lblMsg);
        contentPane.add(opponentPane);
        contentPane.add(mainPane);
        contentPane.add(lblShowTime);
        contentPane.add(btnExit);

        game = new MainGame(mainPane.getWidth(), mainPane.getHeight(), mainPane);
        game.setGameAware(new GGameAware());
        game.init();

        oppoGame = new SmallGame(opponentPane.getWidth(), opponentPane.getHeight(), opponentPane);

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seconds++;
                lblShowTime.setText("Time: " + seconds + "s");
            }
        });


        socket.addObserver(new Observer() {
            public void update(Observable o, Object arg) {

                if (isBattle) {
                    String[] data = (String[]) arg;
                    if (data[0].equals("join")) {
                        lblMsg.setText("对方响应！");

                        socket.send("connect;" + game.getPlateCount());

                    } else if (data[0].equals("connect")) {
                        game.setPlateCount(Integer.parseInt(data[1]));
                        game.reset();

                        oppoGame.setPlateCount(Integer.parseInt(data[1]));
                        oppoGame.init();

                        lblMsg.setText("等待对方开始，游戏即将开始");

                        spinnerPlateCount.setEnabled(false);
                        seconds = 0;
                        timer.stop();

                        socket.send("connected;");

                    } else if (data[0].equals("connected")) {
                        oppoGame.setPlateCount(game.getPlateCount());
                        oppoGame.init();

                        spinnerPlateCount.setEnabled(false);
                        btnStart.setEnabled(true);
                        lblMsg.setText("点击Start，开始游戏");

                    } else if (data[0].equals("start")) {
                        seconds = 0;
                        timer.start();

                        lblMsg.setText("开始比赛！");

                    } else if (data[0].equals("firstClick")) {
                        oppoGame.showTopClearByName(data[1]);

                    } else if (data[0].equals("secondClick")) {
                        oppoGame.moveToStickByName(data[1], data[2]);

                    } else if (data[0].equals("win")) {
                        isOppoWin = true;
                        lblMsg.setText("对方完成了比赛，时间：" + data[1] + "s.");

                    } else if (data[0].equals("exit")) {
                        lblMsg.setText("对方离开了游戏...");

                    } else {
                    }

                }

            }
        });

        this.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                socket.send("exit;");
                socket.close();
            }

            public void windowClosed(WindowEvent e) {
                socket.send("exit;");
                socket.close();
            }

            public void windowActivated(WindowEvent e) {
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    //	private JTextField txtLocalPort;
//	private JTextField txtRemotePort;
    private boolean isOppoWin = false;

    public class GGameAware implements MainGame.GameAware {

        public boolean OnNextLevel(int level) {

            if (isBattle) {
                timer.stop();
                socket.send("win;" + seconds);
                if (isOppoWin) {
                    lblMsg.setText("你输了...");
                } else {
                    lblMsg.setText("很棒！等待对方完成游戏。");
                }

            } else {
                if (timer.isRunning()) {

                    seconds = 0;

                    lblMsg.setText("很棒!!");
//					btnStart.setText("Start");
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Next Level?")) {
                        game.setPlateCount(game.getPlateCount() + 1);
                        spinnerPlateCount.setValue(game.getPlateCount());
                        lblMsg.setText("");
//						return true;
                    } else {
                        spinnerPlateCount.setEnabled(true);
                        timer.stop();
                    }
                }
            }
            return false;
        }

        public void OnFirstClick(String stickName) {
            if (isBattle) {
                socket.send("firstClick;" + stickName);
            }
        }

        public void OnSecondClick(String fromStickName, String targetStickName) {
            if (isBattle) {
                socket.send("secondClick;" + fromStickName + ";" + targetStickName);
            }
        }

        public void OnComplete(int plateCount) {

        }

    }

}
