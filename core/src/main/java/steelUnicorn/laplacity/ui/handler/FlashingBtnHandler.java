package steelUnicorn.laplacity.ui.handler;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Timer;

public class FlashingBtnHandler {
    private int countNum;

    private Button btn;

    private ImageButton.ImageButtonStyle usualImgStyle;
    private final ImageButton.ImageButtonStyle flashedImgStyle;
    private TextButton.TextButtonStyle usualTextBtnStyle;
    private final TextButton.TextButtonStyle flashedTextBtnStyle;
    private Button.ButtonStyle usualBtnStyle;
    private final Button.ButtonStyle flashedBtnStyle;

    private final Timer.Task flashingTask = new Timer.Task() {
        @Override
        public void run() {
            countNum--;
            //Gdx.app.log("Timer flashing", "" + countNum);
            if (btn instanceof ImageButton) {
                ImageButton imgBtn = (ImageButton) btn;
                ImageButton.ImageButtonStyle oldStyle = imgBtn.getStyle();
                imgBtn.setStyle(oldStyle == flashedImgStyle || countNum == 0
                        ? usualImgStyle : flashedImgStyle);
            } else if (btn instanceof TextButton){
                TextButton textButton = (TextButton) btn;
                TextButton.TextButtonStyle oldStyle = textButton.getStyle();
                textButton.setStyle(oldStyle == flashedTextBtnStyle || countNum == 0
                        ? usualTextBtnStyle : flashedTextBtnStyle);
            } else {
                Button.ButtonStyle oldStyle = btn.getStyle();
                btn.setStyle(oldStyle == flashedBtnStyle || countNum == 0 ?
                        usualBtnStyle : flashedBtnStyle);
            }
        }
    };

    public FlashingBtnHandler() {
        btn = null;
        flashedImgStyle = new ImageButton.ImageButtonStyle();
        flashedTextBtnStyle = new TextButton.TextButtonStyle();
        flashedBtnStyle = new Button.ButtonStyle();
    }

    public void setBtn(Button button) {
        btn = button;

        if (btn instanceof ImageButton) {
            usualImgStyle = ((ImageButton) btn).getStyle();
            flashedImgStyle.imageUp = ((ImageButton)btn).getStyle().imageDown;
            flashedImgStyle.imageDown = ((ImageButton)btn).getStyle().imageUp;
        } else if (btn instanceof TextButton){
            usualTextBtnStyle = ((TextButton) btn).getStyle();
            flashedTextBtnStyle.up = ((TextButton)btn).getStyle().down;
            flashedTextBtnStyle.down = ((TextButton)btn).getStyle().up;
            flashedTextBtnStyle.font = usualTextBtnStyle.font;
            flashedTextBtnStyle.fontColor = usualTextBtnStyle.fontColor;
        } else {
            usualBtnStyle = btn.getStyle();
            flashedBtnStyle.up = btn.getStyle().down;
            flashedBtnStyle.down = btn.getStyle().up;
        }
    }

    public void startFlashing(float delay, float interval, int repeatCount) {
        stopFlashing();

        countNum = repeatCount + 1;
        Timer.schedule(flashingTask, delay, interval, repeatCount);
    }

    public void stopFlashing() {
        if (flashingTask.isScheduled()) {
            flashingTask.cancel();
            if (btn instanceof ImageButton) {
                ((ImageButton) btn).setStyle(usualImgStyle);
            } else if (btn instanceof TextButton){
                ((TextButton) btn).setStyle(usualTextBtnStyle);
            } else {
                btn.setStyle(usualBtnStyle);
            }
        }
    }
}
