package org.oilmod.oilforge.multipart.client;

import codechicken.lib.colour.EnumColour;
import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.UVTranslation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.oilmod.oilforge.multipart.TestMultiBlock;
import org.oilmod.oilforge.multipart.MultiPartTileEntity;

public class MultiPartRender extends TileEntityRenderer<MultiPartTileEntity> {

    private static final RenderType buttonType = RenderType.getEntityCutout(new ResourceLocation("multiparttest:textures/wool.png"));
    public MultiPartRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(MultiPartTileEntity tileEntityIn, float partialTicks, MatrixStack ms, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        CCRenderState ccrs = CCRenderState.instance();
        ccrs.brightness = combinedLightIn;
        ccrs.overlay = combinedOverlayIn;
        Matrix4 mat = new Matrix4(ms);

        ccrs.bind(buttonType, bufferIn);

        BlockState state = tileEntityIn.getBlockState();
        int x = state.get(TestMultiBlock.X);
        int y = state.get(TestMultiBlock.Y);
        Direction.Axis axis = state.get(TestMultiBlock.AXIS);
        EnumColour colour = state.get(TestMultiBlock.COLOUR);



        CCModel button = TestMultiBlock.model.copy();
        if (axis != Direction.Axis.X) {
            button.apply(new Translation(-1/16D, -1/16D, -1/16D));
            button.apply(new Rotation(90 * MathHelper.torad, axis == Direction.Axis.Z?Vector3.Y_NEG:Vector3.Z_POS));
            button.apply(new Translation(1/16D, 1/16D, 1/16D));
        }
        button.apply(new Translation(x/16D, y/16D, 0));

        button.render(ccrs, mat, new UVTranslation(0.5 * (colour.getWoolMeta() % 2), 0.125 * (colour.getWoolMeta() / 2)));

        ccrs.reset();
    }


}
