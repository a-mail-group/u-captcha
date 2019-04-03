# u-captcha - public domain java captcha library.


```java
import cgh.maxim.u.captcha.engine.CaptchaWriter;
import cgh.maxim.u.captcha.components.Textgen;

CaptchaWriter cw = new CaptchaWriter();

// You cat skip this! CaptchaWriter will create
// its own Random generator, if none provided.
cw.setPrng(new SecureRandom());
// This also works:
//  cw.setPrng(new Random());

Random random = cw.getPrng();

// Create a 10 Char captcha.
String captchaText = Textgen.Textgen.randomText(random,10);

BufferedImage img = cw.generate(new Point(300,100),captchaText);

OutputStream out = ???;

ImageIO.write(img, "png", out);
```
