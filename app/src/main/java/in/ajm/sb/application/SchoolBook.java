package in.ajm.sb.application;

import android.app.Application;

import in.ajm.sb.BuildConfig;
import in.ajm.sb.data.LocationObject;
import in.ajm.sb.data.User;
import in.ajm.sb.helper.PreferencesManager;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by DSD on 27/03/18.
 */

public class SchoolBook extends Application {

    private static final String REALM_DB_NAME = "sb.realm";
    private static final int REALM_DB_VERSION = 14;
    private static final int OLD_REALM_DB_VERSION = 11;

    User user;
    LocationObject locationObject;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocationObject getLocationObject() {
        return locationObject;
    }

    public void setLocationObject(LocationObject locationObject) {
        this.locationObject = locationObject;
    }

    RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
//            if (oldVersion == 11) {
//                realm.delete("AdminMenu");
//                realm.delete("AdminSubMenu");
//
//                RealmObjectSchema adminMenu = schema.get("AdminMenu");
//                if (adminMenu != null)
//                    adminMenu.addField("id", Integer.class, FieldAttribute.PRIMARY_KEY);
//
//                RealmObjectSchema adminSubMenu = schema.get("AdminSubMenu");
//                if (adminSubMenu != null)
//                    adminSubMenu.addField("id", Integer.class, FieldAttribute.PRIMARY_KEY);
//
//                oldVersion++;
//            }
//            if (oldVersion == 13) {
//                RealmObjectSchema organization = schema.get("Organization");
//                if (organization != null) {
//                    organization.addField("startTime", String.class);
//                }
//
//
//                schema.create("TurnPlan")
//                        .addField("description", String.class)
//            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
//        Realm.init(this);
//        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
//                .name("sb_ig_dsd.realm")
//                .schemaVersion(0)
//                .build();
//        Realm.setDefaultConfiguration(realmConfig);


//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Mark Simonson - Proxima Nova Alt Regular-webfont.ttf");

        Realm.init(this);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .build();
        DynamicRealm dynRealm = DynamicRealm.getInstance(realmConfig);
        long version = dynRealm.getVersion();
        dynRealm.close();

        if (version != -1 && version < OLD_REALM_DB_VERSION) {
            Realm.deleteRealm(realmConfig);
            PreferencesManager.clearAllData(getApplicationContext());
        }

        RealmConfiguration.Builder configBuilder = new RealmConfiguration.Builder()
                .name(REALM_DB_NAME)
                .migration(migration)
                .schemaVersion(REALM_DB_VERSION);

        if (BuildConfig.DEBUG) {
            configBuilder.deleteRealmIfMigrationNeeded();
//            FirebaseCrash.setCrashCollectionEnabled(false);
        }

        Realm.setDefaultConfiguration(configBuilder.build());
    }
}
