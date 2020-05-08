package com.example.faceid;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity  {
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //check if we are returning from picture selection
            if (requestCode == PICKER) {
                //import the image
                //the returned picture URI
                Uri pickedUri = data.getData();
                //declare the bitmap
                Bitmap pic = null;
                //declare the path string
                String imgPath = "";
                //retrieve the string using media data
                String[] medData = { MediaStore.Images.Media.DATA };
                //query the data
                Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
                if(picCursor!=null)
                {
                    //get the path string
                    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    picCursor.moveToFirst();
                    imgPath = picCursor.getString(index);
                }
                else {
                    imgPath = pickedUri.getPath();
                }
                //если у нас есть новый URI, попытка декодировать растровое изображение
                if(pickedUri!=null) {
                    //установить ширину и высоту, которые мы хотим использовать в качестве максимального отображения
                    int targetWidth = 600;
                    int targetHeight = 400;
                    //создать параметры растрового изображения для расчета и использования размера выборки
                    BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
                    //только первые размеры изображения декодирования, а не само растровое изображение
                    bmpOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgPath, bmpOptions);
                    //ширина и высота изображения перед выборкой
                    int currHeight = bmpOptions.outHeight;
                    int currWidth = bmpOptions.outWidth;
                    //переменная для хранения нового размера выборки
                    int sampleSize = 1;
                    //рассчитать размер выборки, если существующий размер больше целевого
                    if (currHeight>targetHeight || currWidth>targetWidth)
                    {
                        //используйте ширину или высоту
                        if (currWidth>currHeight) {
                            sampleSize = Math.round((float) currHeight / (float) targetHeight);
                        }
                        else {
                            sampleSize = Math.round((float) currWidth / (float) targetWidth);
                        }
                        //использовать новый размер выборки
                        bmpOptions.inSampleSize = sampleSize;
                        //получить файл в виде растрового изображения
                        pic = BitmapFactory.decodeFile(imgPath, bmpOptions);
                        //передать растровое изображение в ImageAdapter для добавления в массив
                        imgAdapt.addPic(pic);
                        //перерисовать миниатюры галереи, чтобы отразить новое дополнение
                        picGallery.setAdapter(imgAdapt);
                        //отобразить вновь выбранное изображение в большем размере
                        picView.setImageBitmap(pic);
                        //параметры масштаба
                        picView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }
            }
        }
        //superclass method
        super.onActivityResult(requestCode, resultCode, data);
    }
    // используется для идентификации изображения
    private final int PICKER = 1;
    //Когда пользователь нажимает на эскиз и переходит к выбору изображения
    private int currentPic = 0;
    //gallery object
    private Gallery picGallery;
    //image view for larger display
    private ImageView picView;
    //adapter for gallery view
    private PicAdapter imgAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picView = (ImageView) findViewById(R.id.picture);

        picGallery = findViewById(R.id.gallery);

        //create a new adapter
        imgAdapt = new PicAdapter(this);

        //set the gallery adapter
        picGallery.setAdapter(imgAdapt);

        //установить прослушиватель длинного щелчка для каждого элемента миниатюры галереи
        picGallery.setOnItemLongClickListener(new OnItemLongClickListener() {
            //обрабатывать длинные клики
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                //взять пользователя, чтобы выбрать изображение
                //обновить текущую выбранную позицию, чтобы мы присвоили импортированному растровому изображению правильный элемент
                currentPic = position;

                //перенести пользователя в выбранное ими приложение для выбора изображений (галерея или файловый менеджер)
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                //мы будем обрабатывать возвращенные данные в onActivityResult
                startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);

                return true;
            }
        });
        //установить прослушиватель кликов для каждого элемента в галерее миниатюр
        picGallery.setOnItemClickListener(new OnItemClickListener() {
            //обрабатывать клики
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //установить увеличенное изображение для отображения выбранного растрового метода вызова класса адаптера
                picView.setImageBitmap(imgAdapt.getPic(position));
            }
        });
    }

    //переменная представляет фоновый цвет элемента Android Gallery
    int defaultItemBackground;
    //Context указывает на пользовательский интерфейс
    private Context galleryContext;
    //хранит изображения
    private Bitmap[] imageBitmaps;
    //placeholder bitmap for empty spaces in gallery
    Bitmap placeholder;

    public class PicAdapter extends BaseAdapter {
        public PicAdapter(Context c) {

            //instantiate context
            galleryContext = c;
            //create bitmap array
            imageBitmaps = new Bitmap[10];
            //decode the placeholder image
            placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

            //установить заполнитель как все эскизы в галерее изначально
            for (int i = 0; i < imageBitmaps.length; i++)
                imageBitmaps[i] = placeholder;
            //получить атрибуты стиля - использовать системные ресурсы Android по умолчанию
            TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);

            //получить фоновый ресурс
            defaultItemBackground = styleAttrs.getResourceId(
                    R.styleable.PicGallery_android_galleryItemBackground, 0);

            //перерабатывать атрибуты
            styleAttrs.recycle();

        }

        //возвращает количество объектов в галерее
        public int getCount() {
            return imageBitmaps.length;
        }

        //вернуть товар в указанной позиции
        public Object getItem(int position) {
            return position;
        }

        //вернуть идентификатор товара в указанной позиции
        public long getItemId(int position) {
            return position;
        }

        //get view specifies layout and display options for each thumbnail in the gallery
        public View getView(int position, View convertView, ViewGroup parent) {

            //create the view
            ImageView imageView = new ImageView(galleryContext);
            //указать растровое изображение в этой позиции в массиве
            imageView.setImageBitmap(imageBitmaps[position]);
            //установить параметры макета
            imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
            //тип масштаба в пределах области просмотра
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //установить фон элемента галереи по умолчанию
            imageView.setBackgroundResource(defaultItemBackground);
            //вернуть вид
            return imageView;
        }
        //вспомогательный метод для добавления растрового изображения в галерею, когда пользователь выбирает один
        public void addPic(Bitmap newPic)
        {
            //установить на текущий выбранный индекс
            imageBitmaps[currentPic] = newPic;
        }
        //вернуть растровое изображение в указанной позиции для большего дисплея
        public Bitmap getPic(int posn)
        {
            //вернуть растровое изображение в posn индекс
            return imageBitmaps[posn];
        }
    }
}