package thirdfx.fxgl;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFXGLExample extends GameApplication {


    private static final Logger log = LoggerFactory.getLogger(TestFXGLExample.class);

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);

        settings.setIntroEnabled(true);
    }

    public static void main(String[] args) {
        GameApplication.launch(args);
    }


}
