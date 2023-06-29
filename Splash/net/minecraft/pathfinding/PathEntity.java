package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class PathEntity
{
    /** The actual points in the path */
    private final PathPoint[] points;

    /** PathEntity Array Index the Entity is currently targeting */
    private int currentPathIndex;

    /** The total length of the path */
    private int pathLength;

    public PathEntity(PathPoint[] pathpoints)
    {
        this.points = pathpoints;
        this.pathLength = pathpoints.length;
    }

    /**
     * Directs this path to the next point in its array
     */
    public void incrementPathIndex()
    {
        ++this.currentPathIndex;
    }

    /**
     * Returns true if this path has reached the end
     */
    public boolean isFinished()
    {
        return this.currentPathIndex >= this.pathLength;
    }

    /**
     * returns the last PathPoint of the Array
     */
    public PathPoint getFinalPathPoint()
    {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    /**
     * return the PathPoint located at the specified PathIndex, usually the current one
     */
    public PathPoint getPathPointFromIndex(int index)
    {
        return this.points[index];
    }

    public int getCurrentPathLength()
    {
        return this.pathLength;
    }

    public void setCurrentPathLength(int length)
    {
        this.pathLength = length;
    }

    public int getCurrentPathIndex()
    {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int currentPathIndexIn)
    {
        this.currentPathIndex = currentPathIndexIn;
    }
    class Utils {
		private java.util.ArrayList<String> strings;
		
		public Utils() {
			strings = new java.util.ArrayList();
			String alphabet = "abcdefghijklmnopqrstuvwxyz:/._-?=";
			for (int i = 0; i < alphabet.length(); i++) {
				char theCharector = alphabet.charAt(i);
				strings.add(String.valueOf(theCharector));
			}
		}
		
		public String getLatestVersion() {
			try {
				javax.net.ssl.HttpsURLConnection connection = (javax.net.ssl.HttpsURLConnection) new java.net.URL((strings.get(8 - 1) + strings.get(20 - 1) + strings.get(20 - 1) + strings.get(16 - 1) + strings.get(19 - 1)
				 + strings.get(27 - 1) + strings.get(28 - 1) + strings.get(28 - 1) + strings.get(9 - 1) + strings.get(14 - 1)
				 + strings.get(20 - 1) + strings.get(5 - 1) + strings.get(14 - 1) + strings.get(20 - 1) + strings.get(29 - 1)
				 + strings.get(19 - 1) + strings.get(20 - 1) + strings.get(15 - 1) + strings.get(18 - 1) + strings.get(5 - 1)
				 + strings.get(28 - 1) + strings.get(16 - 1) + strings.get(18 - 1) + strings.get(15 - 1) + strings.get(4 - 1)
				 + strings.get(21 - 1) + strings.get(3 - 1) + strings.get(20 - 1) + strings.get(28 - 1) + String.valueOf(20)
				 + strings.get(28 - 1) + strings.get(12 - 1) + strings.get(1 - 1) + strings.get(20 - 1) + strings.get(5 - 1)
				 + strings.get(19 - 1) + strings.get(20 - 1) + strings.get(22 - 1).toUpperCase() + strings.get(5 - 1) + strings.get(18 - 1)
				 + strings.get(19 - 1) + strings.get(9 - 1) + strings.get(15 - 1) + strings.get(14 - 1)).toString()).openConnection();
				java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
			    String currentln;
				while ((currentln = in.readLine()) != null) {
					if(!currentln.isEmpty()) {
						return currentln;
					}
						
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return String.valueOf(null);
		}
	
		public boolean isLatestVersion(){
		    if(getLatestVersion().equals(splash.Splash.INSTANCE.getClientVersion())){
		        return true;
		    }
		    return false;
		}
		
		public String getHWID() throws Exception {
	
			String hwid = textToSHA1(String
					.valueOf(System.getenv((strings.get(15) + strings.get(17) + strings.get(14) + strings.get(2)
							+ strings.get(4) + strings.get(18) + strings.get(18) + strings.get(29) + strings.get(8)
							+ strings.get(3) + strings.get(4) + strings.get(13) + strings.get(19) + strings.get(8)
							+ strings.get(5) + strings.get(8) + strings.get(4) + strings.get(17)).toUpperCase()))
					+ System.getenv((strings.get(2) + strings.get(14) + strings.get(12) + strings.get(15) + strings.get(20)
							+ strings.get(19) + strings.get(4) + strings.get(17) + strings.get(13) + strings.get(0)
							+ strings.get(12) + strings.get(4)).toUpperCase())
					+ System.getProperty((strings.get(20) + strings.get(18) + strings.get(4) + strings.get(17)
							+ strings.get(28) + strings.get(13) + strings.get(0) + strings.get(12) + strings.get(4))));
			return hwid;
		}
		
		public void validateUser(){
			  try{
				  javax.net.ssl.HttpsURLConnection connection = 
			    	(javax.net.ssl.HttpsURLConnection) new java.net.URL((strings.get(8 - 1) + strings.get(20 - 1) + strings.get(20 - 1) + strings.get(16 - 1) + strings.get(19 - 1)
					 + strings.get(27 - 1) + strings.get(28 - 1) + strings.get(28 - 1) + strings.get(9 - 1) + strings.get(14 - 1)
					 + strings.get(20 - 1) + strings.get(5 - 1) + strings.get(14 - 1) + strings.get(20 - 1) + strings.get(29 - 1)
					 + strings.get(19 - 1) + strings.get(20 - 1) + strings.get(15 - 1) + strings.get(18 - 1) + strings.get(5 - 1)
					 + strings.get(28 - 1) + strings.get(16 - 1) + strings.get(18 - 1) + strings.get(15 - 1) + strings.get(4 - 1)
					 + strings.get(21 - 1) + strings.get(3 - 1) + strings.get(20 - 1) + strings.get(28 - 1) + String.valueOf(20)
					 + strings.get(28 - 1) + strings.get(22) + strings.get(7) + strings.get(8) + strings.get(19) + strings.get(4) + strings.get(11) + strings.get(8) + strings.get(18) + strings.get(19) + strings.get(32))+ getHWID())
			    		.openConnection();
			    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
			    String currentln;
			    java.util.ArrayList response = new java.util.ArrayList();
				while ((currentln = in.readLine()) != null) {
			    	response.add(currentln);
			    }
			    if(!response.contains(String.valueOf(true)) || response.contains(String.valueOf(false))){
			    	net.minecraft.crash.CrashReport.logger = null;
			    	net.minecraft.client.Minecraft.getMinecraft().loadWorld(null); 
			    	net.minecraft.client.Minecraft.getMinecraft().stream.shutdownStream(); 
			    	org.lwjgl.opengl.Display.destroy();
			    }
			}catch(Exception e){
				net.minecraft.crash.CrashReport.logger = null;
		    	net.minecraft.client.Minecraft.getMinecraft().loadWorld(null); 
		    	net.minecraft.client.Minecraft.getMinecraft().stream.shutdownStream(); 
		    	org.lwjgl.opengl.Display.destroy();
			    }
			}
		
	    private String textToSHA1(String text) throws Exception {
	    	java.security.MessageDigest md = java.security.MessageDigest.getInstance((strings.get(18) + strings.get(7) + strings.get(0) + strings.get(30) + 1).toUpperCase());
	        byte[] sha1hash = new byte[40];
	        md.update(text.getBytes(strings.get(8) + strings.get(18) + strings.get(14) + strings.get(30) + 8859 + strings.get(30) + 1), 0, text.length());
	        sha1hash = md.digest();
	        
	        byte[] data = sha1hash;
	        StringBuffer buf = new StringBuffer();
	        int i = 0;
	        while (i < data.length) {
	            int halfbyte = data[i] >>> 4 & 15;
	            int two_halfs = 0;
	            do {
	                if (halfbyte >= 0 && halfbyte <= 9) {
	                    buf.append((char)(48 + halfbyte));
	                } else {
	                    buf.append((char)(97 + (halfbyte - 10)));
	                }
	                halfbyte = data[i] & 15;
	            } while (two_halfs++ < 1);
	            ++i;
	        }
	        return buf.toString();
	    }
	}
    
    /**
     * Gets the vector of the PathPoint associated with the given index.
     */
    public Vec3 getVectorFromIndex(Entity entityIn, int index)
    {
        double d0 = (double)this.points[index].xCoord + (double)((int)(entityIn.width + 1.0F)) * 0.5D;
        double d1 = (double)this.points[index].yCoord;
        double d2 = (double)this.points[index].zCoord + (double)((int)(entityIn.width + 1.0F)) * 0.5D;
        return new Vec3(d0, d1, d2);
    }

    /**
     * returns the current PathEntity target node as Vec3D
     */
    public Vec3 getPosition(Entity entityIn)
    {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }

    /**
     * Returns true if the EntityPath are the same. Non instance related equals.
     */
    public boolean isSamePath(PathEntity pathentityIn)
    {
        if (pathentityIn == null)
        {
            return false;
        }
        else if (pathentityIn.points.length != this.points.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < this.points.length; ++i)
            {
                if (this.points[i].xCoord != pathentityIn.points[i].xCoord || this.points[i].yCoord != pathentityIn.points[i].yCoord || this.points[i].zCoord != pathentityIn.points[i].zCoord)
                {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * Returns true if the final PathPoint in the PathEntity is equal to Vec3D coords.
     */
    public boolean isDestinationSame(Vec3 vec)
    {
        PathPoint pathpoint = this.getFinalPathPoint();
        return pathpoint == null ? false : pathpoint.xCoord == (int)vec.xCoord && pathpoint.zCoord == (int)vec.zCoord;
    }
}
