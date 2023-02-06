package com.kemalakkus.countrylistapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//Bir çok şey kullanacaz. sadece internetten veri çekmek değil roombase vs. de olacak.
// O yüzden tek bir sınıftan öte bütün herşeyi koyabileceğimiz bir kotlin dosyası oluşturcaz

//SQLite'ın içinde RoomDatabase'i kullanabilmek için Entity denilen bir yapıyı oluşturacaz.
//her entity de bir primary key kullanmalıyı. SQLite'ta her veri girişi yapıldığında oluşturulan uniq id'lere primary key denir. Buna göre veri girişini sınırlandırabiliyoruz.
//Birebir aynı değerleri girsek bile 2 ayrı veriyi bu primary key sayesinde birbirinden ayırt edebiliyoruz.

@Entity
data class Country( //data class veri tuttuğumuz sınıflardır. herhangi bi yerdem veri çekerken verimizi modellemek için çok fazla bu sınıf kullanılır.
    @ColumnInfo(name = "name") //oluşturulacak sütunların, oluşturulacak değerlerin isimlerini burada verebiliyoruz
    @SerializedName("name") //veri setimizde name diye tanımlanan veriyi biz countryName diye tanımladık. O yüzden countryName yazdığımız yere name verisi gelsin diye bu yöntemi kullanıyoruz
    val countryName: String?, // en az 1 tane constructor  vermek gerkiyor.
    @ColumnInfo(name = "region")
    @SerializedName("region")
    val countryRegion: String?, // data setimizde bu bilgiler var ona göre constructor oalrak verdik.
    @ColumnInfo(name = "capital")
    @SerializedName("capital")
    val countryCapital: String?,
    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    val countryCurrency: String?,
    @ColumnInfo(name = "language")
    @SerializedName("language")
    val countryLanguage: String?,
    @ColumnInfo(name = "flag")
    @SerializedName("flag")
    val imageUrl: String?){

    //PrimaryKey'i constructor yani Country sınıfının bir parametresi olarak verseydik sürekli model oluşturduğumuzda id vermemiz gerecekti.
    //O yüzden dataclass'ımıza süslü parantez ile body açtık ve orada belirttik
    @PrimaryKey(autoGenerate = true) //ID'yi otomatik olarak verme talimatı
    var uuid: Int = 0 //bir değişken tanımladık

}

//https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

//veri setimizde olan isimler ile Country sınıfında tanımladığımız isimler farklı ve sıraları da karışık.
// @SerializedName'i tanımladığımız değişkenin üzerine koyarak içine veri setindeki ona karşılık gelen değişken adını ekliyoruz














