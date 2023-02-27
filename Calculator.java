import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.*;
import java.lang.Math;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Calculator {

    private static final int WINDOW_WIDTH = 480; // Here we define the width of the window
    private static final int WINDOW_HEIGHT = 600; // Here we define the height of the window
    private static final int BUTTON_WIDTH = 80; // Here we define the width of the buttons
    private static final int BUTTON_HEIGHT = 70; // Here we define the height of the buttons
    private static final int MARGIN_X = 20; // Here we define the default x
    private static final int MARGIN_Y = 60; // Here we define the default y

    private JFrame window; // Main window
    private JTextField inText; // Input
    private RoundedButton btnC, btnBack, btnMod, btnDiv, btnMul, btnSub, btnAdd,
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnPoint, btnEqual, btnPower, btnLog, btnRoot; // Our button declaration

    private char opt = ' '; // Save the operator
    private boolean go = true; // For calculate with Opt != (=)
    private boolean addWrite = true; // Connect numbers in display
    private double val = 0; // Save the value typed for calculation

    static Point mouseDownCompCoords; // For a movable window

    public Calculator() {

        // Adding the calculator main frame.

        window = new JFrame("Calculator");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setUndecorated(true);
        window.setLocationRelativeTo(null); // Move window to center

        final Color mainBack = new Color(32, 32, 32);
        window.setBackground(mainBack);

        ImageIcon img = new ImageIcon("icon/calculator.png");
        window.setIconImage(img.getImage());

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Declaring the main positions we're going to use

        int[] x = { MARGIN_X, MARGIN_X + 90, 200, 290, 380 };
        int[] y = { MARGIN_Y, MARGIN_Y + 100, MARGIN_Y + 180, MARGIN_Y + 260, MARGIN_Y + 340, MARGIN_Y + 420 };

        mouseDownCompCoords = null; // Setting movable window pos to null

        // Adding the Text Field

        inText = new JTextField("0");
        inText.setBounds(x[0], y[0], 400, 70);
        inText.setBackground(mainBack);
        inText.setEditable(false);
        inText.setForeground(Color.white);
        inText.setFont(new Font("Tahoma", Font.PLAIN, 33));
        inText.setHorizontalAlignment(JTextField.RIGHT);
        inText.setBorder(null);
        window.add(inText);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Define custom title bar
        JPanel titleBar = new JPanel();
        titleBar.setBounds(0, 0, WINDOW_WIDTH, 30);
        titleBar.setBackground(Color.DARK_GRAY);

        JLabel title = new JLabel("Calculator");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.PLAIN, 18));

        RoundedButton closeBtn = new RoundedButton("X");
        closeBtn.setFocusable(false);
        closeBtn.setBackground(Color.RED);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));

        RoundedButton onTop = new RoundedButton("Stick");
        onTop.setFocusable(false);
        onTop.setBackground(Color.RED);
        onTop.setForeground(Color.WHITE);
        onTop.setBorderPainted(false);
        onTop.addActionListener(e -> {
            repaintFont();
            boolean isOnTop = window.isAlwaysOnTop();
            window.setAlwaysOnTop(!isOnTop);
            if (!isOnTop) {
                window.setLocation(2000, 100);
            } else {
                window.setLocationRelativeTo(null);
            }
        });

        titleBar.add(title);
        titleBar.add(Box.createHorizontalGlue());
        titleBar.add(closeBtn);
        titleBar.add(onTop);
        window.add(titleBar);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /*  Adding the events to make the window movabale after removing the default frame.
         * Taken from https://www.daniweb.com/programming/software-development/threads/300248/make-a-jframe-draggable-by-it-s-client-area#post1705581
         * All credits go to the respected owner
         *
         *
        */
        window.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }

            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
            }
        });

        window.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                window.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        /* Here we will initialize our buttons */

        btnC = initBtn("C", x[0], y[1], event -> {
            repaintFont();
            inText.setText("0");
            opt = ' ';
            val = 0;
        });

        btnBack = initBtn("<-", x[1], y[1], event -> {
            repaintFont();
            String str = inText.getText();
            StringBuilder str2 = new StringBuilder();
            for (int i = 0; i < (str.length() - 1); i++) {
                str2.append(str.charAt(i));
            }
            if (str2.toString().equals("")) {
                inText.setText("0");
            } else {
                inText.setText(str2.toString());
            }
        });

        btnMod = initBtn("%", x[2], y[1], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '%';
                    go = false;
                    addWrite = false;
                }
        });

        btnDiv = initBtn("/", x[3], y[1], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '/';
                    go = false;
                    addWrite = false;
                } else {
                    opt = '/';
                }
        });

        btn7 = initBtn("7", x[0], y[2], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("7");
                } else {
                    inText.setText(inText.getText() + "7");
                }
            } else {
                inText.setText("7");
                addWrite = true;
            }
            go = true;
        });

        btn8 = initBtn("8", x[1], y[2], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("8");
                } else {
                    inText.setText(inText.getText() + "8");
                }
            } else {
                inText.setText("8");
                addWrite = true;
            }
            go = true;
        });

        btn9 = initBtn("9", x[2], y[2], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("9");
                } else {
                    inText.setText(inText.getText() + "9");
                }
            } else {
                inText.setText("9");
                addWrite = true;
            }
            go = true;
        });

        btnMul = initBtn("*", x[3], y[2], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '*';
                    go = false;
                    addWrite = false;
                } else {
                    opt = '*';
                }
        });

        btn4 = initBtn("4", x[0], y[3], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("4");
                } else {
                    inText.setText(inText.getText() + "4");
                }
            } else {
                inText.setText("4");
                addWrite = true;
            }
            go = true;
        });

        btn5 = initBtn("5", x[1], y[3], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("5");
                } else {
                    inText.setText(inText.getText() + "5");
                }
            } else {
                inText.setText("5");
                addWrite = true;
            }
            go = true;
        });

        btn6 = initBtn("6", x[2], y[3], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("6");
                } else {
                    inText.setText(inText.getText() + "6");
                }
            } else {
                inText.setText("6");
                addWrite = true;
            }
            go = true;
        });

        btnSub = initBtn("-", x[3], y[3], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }

                    opt = '-';
                    go = false;
                    addWrite = false;
                } else {
                    opt = '-';
                }
        });
        btn1 = initBtn("1", x[0], y[4], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("1");
                } else {
                    inText.setText(inText.getText() + "1");
                }
            } else {
                inText.setText("1");
                addWrite = true;
            }
            go = true;
        });
        btn2 = initBtn("2", x[1], y[4], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("2");
                } else {
                    inText.setText(inText.getText() + "2");
                }
            } else {
                inText.setText("2");
                addWrite = true;
            }
            go = true;
        });
        btn3 = initBtn("3", x[2], y[4], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("3");
                } else {
                    inText.setText(inText.getText() + "3");
                }
            } else {
                inText.setText("3");
                addWrite = true;
            }
            go = true;
        });
        btnAdd = initBtn("+", x[3], y[4], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '+';
                    go = false;
                    addWrite = false;
                } else {
                    opt = '+';
                }
        });

        btnPower = initBtn("^", x[4], y[1], event -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '^';
                    go = false;
                    addWrite = false;
                } else {
                    opt = '^';
                }
        });

        btnLog = initBtn("ln", x[4], y[2], event -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = Math.log(Double.parseDouble(inText.getText()));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = 'l';
                    addWrite = false;
                }
        });
        btnLog.setSize(BUTTON_WIDTH, 2 * BUTTON_HEIGHT + 10);

        btnRoot = initBtn("√", x[4], y[4], event -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = Math.sqrt(Double.parseDouble(inText.getText()));
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '√';
                    addWrite = false;
                }
        });
        btnRoot.setSize(BUTTON_WIDTH, 2 * BUTTON_HEIGHT + 10);


        btnPoint = initBtn(".", x[0], y[5], event -> {
            repaintFont();
            if (addWrite) {
                if (!inText.getText().contains(".")) {
                    inText.setText(inText.getText() + ".");
                }
            } else {
                inText.setText("0.");
                addWrite = true;
            }
            go = true;
        });
        btn0 = initBtn("0", x[1], y[5], event -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText("0");
                } else {
                    inText.setText(inText.getText() + "0");
                }
            } else {
                inText.setText("0");
                addWrite = true;
            }
            go = true;
        });
        btnEqual = initBtn("=", x[2], y[5], event -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
                        inText.setText(String.valueOf((int) val));
                    } else {
                        inText.setText(String.valueOf(val));
                    }
                    opt = '=';
                    addWrite = false;
                }
        });

        btnEqual.setSize(2 * BUTTON_WIDTH + 10, BUTTON_HEIGHT);

        window.setLayout(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /* Here is how we design our buttons, it extends the JButton class. */

    private RoundedButton initBtn(String label, int x, int y, ActionListener event) {
        RoundedButton btn = new RoundedButton(label);
        btn.setBorder(null);
        btn.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(event);
        btn.setFocusable(false);
        final Color primaryDarkColor = new Color(50, 50, 50);
        final Color txtcolor = new Color(160, 160, 160);
        window.getContentPane().setBackground(null);
        btn.setBackground(primaryDarkColor);
        btn.setForeground(txtcolor);

        window.add(btn);

        return btn;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /* Here we do the calculations, it supports addition, substraction, multiplication, division, modulo, and raised to the power */
    public double calc(double x, String input, char opt) {
        inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
        double y = Double.parseDouble(input);
        switch (opt) {
            case '+' -> {
                return x + y;
            }
            case '-' -> {
                return x - y;
            }
            case '*' -> {
                return x * y;
            }
            case '/' -> {
                return x / y;
            }
            case '%' -> {
                return x % y;
            }
            case '^' -> {
                return Math.pow(x, y);
            }
            default -> {
                inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
                return y;
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /* Here we're editing the JButton class, to make rounded edges on the buttons */

    public class RoundedButton extends JButton {
        private Color backgroundColor;

        public RoundedButton() {
            this(null);
        }

        public RoundedButton(String text) {
            super(text);
            setOpaque(false);
            backgroundColor = new Color(50, 50, 50);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }

        public void setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void repaintFont() {
        inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
