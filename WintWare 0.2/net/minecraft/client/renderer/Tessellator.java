package net.minecraft.client.renderer;

public class Tessellator {
   private final BufferBuilder worldRenderer;
   private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
   private static final Tessellator INSTANCE = new Tessellator(2097152);

   public static Tessellator getInstance() {
      return INSTANCE;
   }

   public Tessellator(int bufferSize) {
      this.worldRenderer = new BufferBuilder(bufferSize);
   }

   public void draw() {
      this.worldRenderer.finishDrawing();
      this.vboUploader.draw(this.worldRenderer);
   }

   public BufferBuilder getBuffer() {
      return this.worldRenderer;
   }
}
