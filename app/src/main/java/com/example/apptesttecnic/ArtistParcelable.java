package com.example.apptesttecnic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
//Creamos una classe ArtistParcelable, con características similares a la clase Artis
// para poder guardar los datos de cada artista y pasarlos entre intents
public class ArtistParcelable extends ArrayList<Parcelable> implements Parcelable {
    private int id;
    private final String email,first_name,last_name,description,image;

    public ArtistParcelable(int id, String email, String first_name, String last_name, String description, String image) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }


    public String getLast_name() {
        return last_name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    protected ArtistParcelable(Parcel in) {
        id = in.readInt();
        email = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        description = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(description);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArtistParcelable> CREATOR = new Creator<ArtistParcelable>() {
        @Override
        public ArtistParcelable createFromParcel(Parcel in) {
            return new ArtistParcelable(in);
        }

        @Override
        public ArtistParcelable[] newArray(int size) {
            return new ArtistParcelable[size];
        }
    };

}
