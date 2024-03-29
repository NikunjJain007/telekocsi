package com.alma.telekocsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alma.telekocsi.checking.PreferencesChecking;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class PreferencesSettings extends ARunnableActivity {
	
	private static final int CHECKING = 1;

	private OnClickListener onClickListener = null;
	private Button backButton;
	private Button registeringSubmitButton;
	private RadioGroup smoker;
	private RadioGroup animals;
	private RadioGroup music;
	private RadioGroup discussion;
	private RadioGroup detours;
	private Session session;
	private Profil profile;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.preferences_settings);

        smoker = (RadioGroup)findViewById(R.id.smoker_radio_group);
        animals = (RadioGroup)findViewById(R.id.animals_radio_group);
        detours = (RadioGroup)findViewById(R.id.detours_radio_group);
        discussion = (RadioGroup)findViewById(R.id.discussion_radio_group);
        music = (RadioGroup)findViewById(R.id.music_radio_group);
        
        backButton = (Button)findViewById(R.id.preferences_settings_back_button);
        backButton.setOnClickListener(getOnClickListener()); 
         
        registeringSubmitButton = (Button)findViewById(R.id.registering_submit_button);
        registeringSubmitButton.setOnClickListener(getOnClickListener());
        session = SessionFactory.getCurrentSession(this);
        
        //Valeur courantes en cas d'�dition du profil
        initValues();
    }
	
	/**
	 * remplir les champs avec les valeurs courantes
	 */
	private void initValues(){
		Profil profile = session.getActiveProfile();
		if(profile!=null && session.isConnected()){
			//smoker
			String val  = Profile.getStringVal(profile.getFumeur());
			for(int i=0;i<smoker.getChildCount();i++){
				RadioButton rb = (RadioButton)smoker.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}
			
			//animals
			val  =  Profile.getStringVal(profile.getAnimaux());
			for(int i=0;i<animals.getChildCount();i++){
				RadioButton rb = (RadioButton)animals.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}

			//detours
			val  =  Profile.getStringVal(profile.getDetours());
			for(int i=0;i<detours.getChildCount();i++){
				RadioButton rb = (RadioButton)detours.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}
			
			//music
			val  =  Profile.getStringVal(profile.getMusique());
			for(int i=0;i<music.getChildCount();i++){
				RadioButton rb = (RadioButton)music.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}

			//discussion
			val  =  Profile.getStringVal(profile.getDiscussion());
			for(int i=0;i<discussion.getChildCount();i++){
				RadioButton rb = (RadioButton)discussion.getChildAt(i);
				rb.setChecked(rb.getText().toString().equals(val));
			}
		}else {
				((RadioButton)smoker.getChildAt(2)).setChecked(true);
				((RadioButton)animals.getChildAt(2)).setChecked(true);
				((RadioButton)detours.getChildAt(2)).setChecked(true);
				((RadioButton)music.getChildAt(2)).setChecked(true);
				((RadioButton)discussion.getChildAt(2)).setChecked(true);
		}
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			startShowingMainMenu();
    			break;
    		}
    	}
    	stopProgressDialog();
    }
	
	private OnClickListener getOnClickListener(){
		if(onClickListener==null){
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	private OnClickListener makeOnClickListener(){
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==backButton){
					goBack();
				}
				else if(v==registeringSubmitButton){
					startPreferencesChecking();
				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
	private void startPreferencesChecking(){
		startProgressDialogInNewThread(this);
	}
	
	private void startShowingMainMenu(){
		saveSettings();
		startActivity(new Intent(this, MainMenu.class));
	}

	private void saveSettings(){
		profile = session.getActiveProfile();
		Intent intent = getIntent();
		if(profile==null){
			//creation profil
			Log.d(getClass().getSimpleName(), "Création de profil");
			profile = new Profil();
			
			profile.setPointsDispo(20);
			profile.setClassementMoyen(0);
			profile.setNombreAvis(0);
			profile.setClasseVehicule(0);
			profile.setVehicule("");
			profile.setTypeProfil("C");
			profile.setTypeProfilHabituel("C");
			profile.setPathPhoto("");
			profile.setEmail(intent.getStringExtra("email"));
			profile.setPseudo(intent.getStringExtra("email"));
			profile.setMotDePasse(intent.getStringExtra("password"));
		}
		
		if(!session.isConnected()){
			profile.setConnecte(true);
		}
		
		profile.setSexe(intent.getStringExtra("sexe"));
		profile.setNom(intent.getStringExtra("name"));
		profile.setPrenom(intent.getStringExtra("firstName"));
		
		if(smoker!=null){
			RadioButton rb = (RadioButton)findViewById(smoker.getCheckedRadioButtonId());
			if(rb!=null){
				profile.setFumeur(Profile.getBdVal(rb.getText().toString()));
			}
		}
		if(animals!=null){
			RadioButton rb = (RadioButton)findViewById(animals.getCheckedRadioButtonId());
			if(rb!=null){
				profile.setAnimaux(Profile.getBdVal(rb.getText().toString()));
			}
		}
		if(detours!=null){
			RadioButton rb = (RadioButton)findViewById(detours.getCheckedRadioButtonId());
			if(rb!=null){
				profile.setDetours(Profile.getBdVal(rb.getText().toString()));
			}
		}
		if(discussion!=null){
			RadioButton rb = (RadioButton)findViewById(discussion.getCheckedRadioButtonId());
			if(rb!=null){
				profile.setDiscussion(Profile.getBdVal(rb.getText().toString()));
			}
		}
		if(music!=null){
			RadioButton rb = (RadioButton)findViewById(music.getCheckedRadioButtonId());
			if(rb!=null){
				profile.setMusique(Profile.getBdVal(rb.getText().toString()));
			}
		}
		profile.setDateNaissance(String.format("%s/%s/%s"
				, intent.getStringExtra("jj")
				,intent.getStringExtra("mm")
				,intent.getStringExtra("aaaa")));
		
		profile.setTelephone(intent.getStringExtra("phone"));
	
		System.out.println(profile);
		Log.i(getClass().getName(), profile.toString());
		
		session.saveProfile(profile);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		session = SessionFactory.getCurrentSession(this);
		if(session.isConnected()){
			MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.options_menu, menu);
		    return true;
		}
		return false;
	}

	@Override
	public void run() {
		Intent intent = new Intent(this, PreferencesChecking.class);
    	startActivityForResult(intent, CHECKING);
	}

	
}

