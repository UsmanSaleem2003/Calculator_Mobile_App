package com.example.firstapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private TextView displayTextView;
    private String currentInput = "";
    private double firstNumber = 0;
    private String currentOperator = "";
    private boolean isNewInput = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize display TextView
        displayTextView = findViewById(R.id.displayTextView);

        // Set click listeners for number buttons
        int[] numberButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        };
        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(this);
        }

        // Set click listeners for other buttons
        int[] otherButtonIds = {
                R.id.btn_ce, R.id.btn_c, R.id.btn_back,
                R.id.btn_equals, R.id.btn_add, R.id.btn_subtract,
                R.id.btn_multiply, R.id.btn_divide, R.id.btn_decimal,
                R.id.btn_percent, R.id.btn_reciprocal, R.id.btn_square,
                R.id.btn_sqrt, R.id.btn_sign
        };
        for (int id : otherButtonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_0) appendNumber("0");
        else if (id == R.id.btn_1) appendNumber("1");
        else if (id == R.id.btn_2) appendNumber("2");
        else if (id == R.id.btn_3) appendNumber("3");
        else if (id == R.id.btn_4) appendNumber("4");
        else if (id == R.id.btn_5) appendNumber("5");
        else if (id == R.id.btn_6) appendNumber("6");
        else if (id == R.id.btn_7) appendNumber("7");
        else if (id == R.id.btn_8) appendNumber("8");
        else if (id == R.id.btn_9) appendNumber("9");
        else if (id == R.id.btn_decimal) appendDecimal();
        else if (id == R.id.btn_ce) clearEntry();
        else if (id == R.id.btn_c) clearAll();
        else if (id == R.id.btn_back) backspace();
        else if (id == R.id.btn_add) setOperator("+");
        else if (id == R.id.btn_subtract) setOperator("-");
        else if (id == R.id.btn_multiply) setOperator("*");
        else if (id == R.id.btn_divide) setOperator("/");
        else if (id == R.id.btn_equals) calculateResult();
        else if (id == R.id.btn_percent) applyPercentage();
        else if (id == R.id.btn_reciprocal) applyReciprocal();
        else if (id == R.id.btn_square) applySquare();
        else if (id == R.id.btn_sqrt) applySquareRoot();
        else if (id == R.id.btn_sign) toggleSign();
    }

    private void appendNumber(String number) {
        if (isNewInput) {
            currentInput = "";
            isNewInput = false;
        }
        currentInput += number;
        updateDisplay();
    }

    private void appendDecimal() {
        if (isNewInput) {
            currentInput = "0";
            isNewInput = false;
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
            updateDisplay();
        }
    }

    private void updateDisplay() {
        if (currentInput.isEmpty()) {
            displayTextView.setText("0");
        } else {
            displayTextView.setText(currentInput);
        }
    }

    private void clearEntry() {
        currentInput = "";
        isNewInput = true;
        updateDisplay();
    }

    private void clearAll() {
        currentInput = "";
        firstNumber = 0;
        currentOperator = "";
        isNewInput = true;
        updateDisplay();
    }

    private void backspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            updateDisplay();
        }
    }

    private void setOperator(String operator) {
        if (!currentInput.isEmpty()) {
            firstNumber = Double.parseDouble(currentInput);
            currentOperator = operator;
            isNewInput = true;
        }
    }

    private void calculateResult() {
        if (!currentOperator.isEmpty() && !isNewInput) {
            double secondNumber = Double.parseDouble(currentInput);
            double result = 0;

            switch (currentOperator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        displayTextView.setText(R.string.error);
                        clearAll();
                        return;
                    }
                    break;
            }

            currentInput = removeTrailingZeros(String.format(Locale.US, "%.2f", result));
            if (currentInput.endsWith(".00")) {
                currentInput = currentInput.replace(".00", "");
            }
            currentOperator = "";
            isNewInput = true;
            updateDisplay();
        }
    }

    private String removeTrailingZeros(String value) {
        if (!value.contains(".")) return value;
        value = value.replaceAll("0+$", "");
        if (value.endsWith(".")) value = value.substring(0, value.length() - 1);
        return value;
    }

    private void applyPercentage() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value /= 100;
            currentInput = removeTrailingZeros(String.valueOf(value));
            updateDisplay();
            isNewInput = true;
        }
    }

    private void applyReciprocal() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            if (value != 0) {
                value = 1 / value;
                currentInput = removeTrailingZeros(String.valueOf(value));
                updateDisplay();
                isNewInput = true;
            } else {
                displayTextView.setText(R.string.error);
                clearAll();
            }
        }
    }

    private void applySquare() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value *= value;
            currentInput = removeTrailingZeros(String.valueOf(value));
            updateDisplay();
            isNewInput = true;
        }
    }

    private void applySquareRoot() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            if (value >= 0) {
                value = Math.sqrt(value);
                currentInput = removeTrailingZeros(String.valueOf(value));
                updateDisplay();
                isNewInput = true;
            } else {
                displayTextView.setText(R.string.error);
                clearAll();
            }
        }
    }

    private void toggleSign() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value *= -1;
            currentInput = removeTrailingZeros(String.valueOf(value));
            updateDisplay();
            isNewInput = true;
        }
    }
}