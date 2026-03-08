package com.example.scientificcalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bac, bc, bb1, bb2, bsin, bcos, btan, blog, bln, bfact, bsquare, bsqrt, binv, bdiv, bplus, bmin, bmul, bequal, bpi, bdot;
    TextView tvmain, tvsec;
    String pi = "3.1415926535897932384626433";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        b0 = findViewById(R.id.b0);
        bac = findViewById(R.id.bac);
        bc = findViewById(R.id.bc);
        bb1 = findViewById(R.id.bb1);
        bb2 = findViewById(R.id.bb2);
        bsin = findViewById(R.id.bsin);
        bcos = findViewById(R.id.bcos);
        btan = findViewById(R.id.btan);
        blog = findViewById(R.id.blog);
        bln = findViewById(R.id.bln);
        bfact = findViewById(R.id.bfact);
        bsquare = findViewById(R.id.bsquare);
        bsqrt = findViewById(R.id.bsqrt);
        binv = findViewById(R.id.binv);
        bdiv = findViewById(R.id.bdiv);
        bplus = findViewById(R.id.bplus);
        bmin = findViewById(R.id.bmin);
        bmul = findViewById(R.id.bmul);
        bequal = findViewById(R.id.bequal);
        bpi = findViewById(R.id.bpi);
        bdot = findViewById(R.id.bdot);

        tvmain = findViewById(R.id.tvmain);
        tvsec = findViewById(R.id.tvsec);

        //onclick listeners for all buttons
        b1.setOnClickListener(v -> tvmain.append("1"));
        b2.setOnClickListener(v -> tvmain.append("2"));
        b3.setOnClickListener(v -> tvmain.append("3"));
        b4.setOnClickListener(v -> tvmain.append("4"));
        b5.setOnClickListener(v -> tvmain.append("5"));
        b6.setOnClickListener(v -> tvmain.append("6"));
        b7.setOnClickListener(v -> tvmain.append("7"));
        b8.setOnClickListener(v -> tvmain.append("8"));
        b9.setOnClickListener(v -> tvmain.append("9"));
        b0.setOnClickListener(v -> tvmain.append("0"));
        bac.setOnClickListener(v -> {
            tvmain.setText("");
            tvsec.setText("");
        });
        bc.setOnClickListener(v -> {
            String val = tvmain.getText().toString();
            if (!val.isEmpty()) {
                val = val.substring(0, val.length() - 1);
                tvmain.setText(val);
            }
        });
        bb1.setOnClickListener(v -> tvmain.append("("));
        bb2.setOnClickListener(v -> tvmain.append(")"));
        bsin.setOnClickListener(v -> tvmain.append("sin"));
        bcos.setOnClickListener(v -> tvmain.append("cos"));
        btan.setOnClickListener(v -> tvmain.append("tan"));
        blog.setOnClickListener(v -> tvmain.append("log"));
        bln.setOnClickListener(v -> tvmain.append("ln"));
        bfact.setOnClickListener(v -> {
            String valStr = tvmain.getText().toString();
            if (!valStr.isEmpty()) {
                int val = Integer.parseInt(valStr);
                int fact = factorial(val);
                tvmain.setText(String.valueOf(fact));
                tvsec.setText(String.format(Locale.US, "%d!", val));
            }
        });
        bsquare.setOnClickListener(v -> {
            String valStr = tvmain.getText().toString();
            if (!valStr.isEmpty()) {
                double d = Double.parseDouble(valStr);
                double square = d * d;
                tvmain.setText(String.valueOf(square));
                tvsec.setText(String.format(Locale.US, "%f^2", d));
            }
        });
        bsqrt.setOnClickListener(v -> {
            String val = tvmain.getText().toString();
            if (!val.isEmpty()) {
                double r = Math.sqrt(Double.parseDouble(val));
                tvmain.setText(String.valueOf(r));
            }
        });
        binv.setOnClickListener(v -> tvmain.append("^(-1)"));
        bdiv.setOnClickListener(v -> tvmain.append("÷"));
        bplus.setOnClickListener(v -> tvmain.append("+"));
        bmin.setOnClickListener(v -> tvmain.append("-"));
        bmul.setOnClickListener(v -> tvmain.append("×"));
        bequal.setOnClickListener(v -> {
            String val = tvmain.getText().toString();
            String replacedstr = val.replace('÷', '/').replace('×', '*');
            double result = eval(replacedstr);
            tvsec.setText(val);
            tvmain.setText(String.valueOf(result));
        });
        bpi.setOnClickListener(v -> {
            tvsec.setText(bpi.getText());
            tvmain.append(pi);
        });
        bdot.setOnClickListener(v -> tvmain.append("."));
    }

    //Factorial Function
    int factorial(int n) {
        return (n == 1 || n == 0) ? 1 : n * factorial(n - 1);
    }

    //Eval Function
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        case "log":
                            x = Math.log10(x);
                            break;
                        case "ln":
                            x = Math.log(x);
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
