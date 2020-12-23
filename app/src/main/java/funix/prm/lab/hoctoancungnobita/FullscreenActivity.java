package funix.prm.lab.hoctoancungnobita;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
//            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
//        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        doMyThings();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
//----------------------------------------------------------------------------------

    int trueAnswer;
    int mode = 1;
    TextView question, score;
    TextInputEditText answer;
    MaterialButton checkBtn;
    MediaPlayer right, wrong, countryside;

    RadioGroup selectionMode;

    private void doMyThings() {
        right = MediaPlayer.create(this, R.raw.right);
        wrong = MediaPlayer.create(this, R.raw.wrong);
        countryside = MediaPlayer.create(this, R.raw.cricketsound);

        question = findViewById(R.id.question);
        score = findViewById(R.id.score);
        answer = findViewById(R.id.answer);
        checkBtn = findViewById(R.id.check);

        selectionMode = findViewById(R.id.modeSelection);

        generateNewQuestion();
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSendButton();
            }
        });
        answer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    handleSendButton();
                    handled = true;
                }
                return handled;
            }
        });

        selectionMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                score.setText("0");
                answer.setText("");
                mode = checkedId;
            }
        });

    }

    private void handleSendButton() {
        if(answer.getText().toString().length() < 1) {
            //playsound
            Snackbar.make(question, "Hư thối", Snackbar.LENGTH_LONG).show();
            countryside.start();
            answer.setText("");
            return;
        }
        try {
            if(trueAnswer == Integer.parseInt(answer.getText().toString())){
                //playsound
                right.start();
                generateNewQuestion();
                score.setText(String.valueOf(Integer.parseInt(score.getText().toString()) + 1));
            } else {
                //playsound
                wrong.start();
                score.setText("0");
            }
            answer.setText("");
            return;
        } catch (Exception e) {
            //playsound
            wrong.start();
            answer.setText("");
            return;
        }
    }

//    public void toggleButtonHandler(View view) {
//        //reset
//        score.setText("0");
//        answer.setText("");
//        SwitchMaterial switchMaterial = (SwitchMaterial) view;
//        mode = switchMaterial.isChecked();
//    }
    
    private void generateNewQuestion() {
        int num1 = 0, num2 = 0;
        Random random = new Random();
        switch (mode) {
            case 1:
                // 2 x 1
                num1 = random.nextInt(99) + 1;  // 1 -> 99
                num2 = random.nextInt(9) + 1; // 1 -> 9
                question.setText(num1 + " x " + num2 + " = ");
                trueAnswer = num1 * num2;
                break;
            case 2:
                // 2 x 2
                num1 = random.nextInt(99) + 1;  // 1 -> 99
                num2 = random.nextInt(99) + 1; // 1 -> 99
                question.setText(num1 + " x " + num2 + " = ");
                trueAnswer = num1 * num2;
                break;
            case 3:
                // 3 x 2
                num1 = random.nextInt(999) + 1;  // 1 -> 999
                num2 = random.nextInt(99) + 1; // 1 -> 99
                question.setText(num1 + " x " + num2 + " = ");
                trueAnswer = num1 * num2;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
    }
}
