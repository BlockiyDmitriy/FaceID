using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Accord.Imaging.Filters;
using Accord.Vision.Detection;
using Accord.Vision.Detection.Cascades;

namespace FaceID
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();

            const string pathInputImage = "input.jpg";
            const string pathOutputImage = "output.jpg";

            // загружаем изображение
            Bitmap image = new Bitmap(pathInputImage);

            HaarObjectDetector faceDetector = new HaarObjectDetector(
                      // указываем, что нужно искать лица
                      new FaceHaarCascade(),
                      // устанавливаем возможный минимальный размер
                      minSize: 25,
                      // устанавливаем режим поиска, при котором если область
                      // обнаружена, то поиск во внутренних и пересекающихся
                      // областях производиться не будет. Это позволяет сэкономить время
                      // работы, но если лица находятся очень близко друг от друга, то
                      // возможно потребуется другой режим
                      searchMode: ObjectDetectorSearchMode.NoOverlap);

            // создаём объект, который нужен для выделения объектов на изображении
            RectanglesMarker facesMarker = new RectanglesMarker(Color.Red);
            // распознаём лица
            facesMarker.Rectangles = faceDetector.ProcessFrame(image);
            // выделяем лица на изображении
            facesMarker.ApplyInPlace(image);

            // сохраняем изображение
            image.Save(pathOutputImage);
        }
    }
}
