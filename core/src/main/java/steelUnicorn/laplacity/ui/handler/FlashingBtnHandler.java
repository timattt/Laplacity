package steelUnicorn.laplacity.ui.handler;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Timer;

public class FlashingBtnHandler {
    private int countNum;

    private Button btn;

    private boolean isImageButton;
    private ImageButton.ImageButtonStyle usualImgStyle;
    private final ImageButton.ImageButtonStyle flashedImgStyle;
    private Button.ButtonStyle usualBtnStyle;
    private final Button.ButtonStyle flashedBtnStyle;

    private final Timer.Task flashingTask = new Timer.Task() {
        @Override
        public void run() {
            countNum--;
            //Gdx.app.log("Timer flashing", "" + countNum);
            if (isImageButton) {
                ImageButton imgBtn = (ImageButton) btn;
                ImageButton.ImageButtonStyle oldStyle = imgBtn.getStyle();
                imgBtn.setStyle(oldStyle == flashedImgStyle || countNum == 0
                        ? usualImgStyle : flashedImgStyle);
            } else {
                Button.ButtonStyle oldStyle = btn.getStyle();
                btn.setStyle(oldStyle == flashedBtnStyle || countNum == 0 ?
                        usualBtnStyle : flashedBtnStyle);
            }
        }
    };

    public FlashingBtnHandler() {
        btn = null;
        isImageButton = false;
        flashedImgStyle = new ImageButton.ImageButtonStyle();
        flashedBtnStyle = new Button.ButtonStyle();
    }

    public void setBtn(Button button) {
        btn = button;
        isImageButton = btn instanceof ImageButton;

        if (isImageButton) {
            usualImgStyle = ((ImageButton) btn).getStyle();
            flashedImgStyle.imageUp = ((ImageButton)btn).getStyle().imageDown;
            flashedImgStyle.imageDown = ((ImageButton)btn).getStyle().imageUp;
        } else {
            usualBtnStyle = btn.getStyle();
            flashedBtnStyle.up = btn.getStyle().down;
            flashedBtnStyle.down = btn.getStyle().up;
        }
    }

    public void startFlashing(float delay, float interval, int repeatCount) {
        if (!flashingTask.isScheduled()) {
            countNum = repeatCount + 1;
            Timer.schedule(flashingTask, delay, interval, repeatCount);
        }
    }

    public void stopFlashing() {
        if (flashingTask.isScheduled()) {
            flashingTask.cancel();
            if (isImageButton) {
                ((ImageButton) btn).setStyle(usualImgStyle);
            } else {
                btn.setStyle(usualBtnStyle);
            }
        }
    }
}
