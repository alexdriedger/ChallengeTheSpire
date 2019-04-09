package challengeTheSpire;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class ArrowOptionsButton implements IUIElement {

    private Texture arrow;
    private int x;
    private int y;
    private int w;
    private int h;

    private Hitbox hitbox;

    private OnClick onClick;

    public interface OnClick {
        void onClick();
    }

    public ArrowOptionsButton (String imgUrl, int x, int y, OnClick onClick) {
        this.arrow = ImageMaster.loadImage(imgUrl);
        this.x = x;
        this.y = y;
        this.w = (int)(Settings.scale * arrow.getWidth());
        this.h = (int)(Settings.scale * arrow.getHeight());
        this.hitbox = new Hitbox(x,y,w,h);
        this.onClick = onClick;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(arrow,x,y,w,h);
        hitbox.render(sb);
    }

    @Override
    public void update() {
        hitbox.update();
        if(this.hitbox.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            this.onClick.onClick();
        }
    }

    @Override
    public int renderLayer() {
        return 0;
    }

    @Override
    public int updateOrder() {
        return 0;
    }
}
