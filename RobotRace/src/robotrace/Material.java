package robotrace;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     */
    GOLD (
        new float[] {RGBToFloat(191), RGBToFloat(155), RGBToFloat(48), 1.0f},
        new float[] {RGBToFloat(255), RGBToFloat(191), RGBToFloat(0), 1.0f},     
        100f),
    
    /**
     * Silver material properties.
     */
    SILVER (
        new float[] {RGBToFloat(158), RGBToFloat(158), RGBToFloat(158), 1.0f},
        new float[] {RGBToFloat(189), RGBToFloat(189), RGBToFloat(189), 1.0f},     
      128),

    /** 
     * Wood material properties.
     */
    WOOD (
        new float[] {RGBToFloat(62), RGBToFloat(39), RGBToFloat(35), 1.0f},
        new float[] {RGBToFloat(78), RGBToFloat(52), RGBToFloat(46), 1.0f},     
      1f),

    /**
     * Orange material properties.
     */
    ORANGE (
        new float[] {RGBToFloat(255), RGBToFloat(152), RGBToFloat(0), 1.0f},
        new float[] {RGBToFloat(255), RGBToFloat(167), RGBToFloat(38), 1.0f},     
            128f),
    
     /**
     * Red material properties.
     */
    RED (
        new float[] {RGBToFloat(244), RGBToFloat(67), RGBToFloat(54), 1.0f},
        new float[] {RGBToFloat(239), RGBToFloat(83), RGBToFloat(80), 1.0f},     
        100f),
    
    /**
     * Green material properties.
     */
    GREEN (
        new float[] {RGBToFloat(76), RGBToFloat(175), RGBToFloat(80), 1.0f},
        new float[] {RGBToFloat(102), RGBToFloat(187), RGBToFloat(106), 1.0f},     
        100f),
    
    /**
     * Blue material properties.
     */
     BLUE (
         new float[] {RGBToFloat(33), RGBToFloat(150), RGBToFloat(243), 1.0f},
        new float[] {RGBToFloat(66), RGBToFloat(165), RGBToFloat(245), 1.0f},     
       100f),
     
      /**
     * Yellow material properties.
     */
     YELLOW (
        new float[] {RGBToFloat(255), RGBToFloat(235), RGBToFloat(59), 1.0f},
        new float[] {RGBToFloat(255), RGBToFloat(238), RGBToFloat(88), 1.0f},     
      100f),
     
       /**
     * Black material properties.
     */
     BLACK (
        new float[] {RGBToFloat(0), RGBToFloat(0), RGBToFloat(0), 1.0f},
        new float[] {RGBToFloat(0), RGBToFloat(0), RGBToFloat(0), 1.0f},     
      100f),
     
       /**
     * White material properties.
     */
     WHITE (
        new float[] {RGBToFloat(255), RGBToFloat(255), RGBToFloat(255), 1.0f},
        new float[] {RGBToFloat(255), RGBToFloat(255), RGBToFloat(255), 1.0f},     
      100f),
     
     GREY (
        new float[] {RGBToFloat(127), RGBToFloat(127), RGBToFloat(127), 1.0f},
        new float[] {RGBToFloat(127), RGBToFloat(127), RGBToFloat(127), 1.0f},     
      100f);
     
    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

     private static float RGBToFloat(int rgb)
    {
        //Returns the rbg value calculated with an extensive and difficult formula.
        return rgb/255f;
    }
}

