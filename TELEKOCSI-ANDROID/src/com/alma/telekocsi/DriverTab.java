package com.alma.telekocsi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DriverTab extends OptionsMenu {
	
	private OnClickListener onClickListener = null;
	private Button routeActivationButton;
	private Button routeCreationButton;
	private Button driverTransactionButton;
	private Button routeModificationButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onglet_conducteur);

		routeActivationButton = (Button)findViewById(R.id.route_activation_button);
		routeActivationButton.setOnClickListener(getOnClickListener());
		
		driverTransactionButton = (Button)findViewById(R.id.driver_transaction_button);
		driverTransactionButton.setOnClickListener(getOnClickListener());
		
		routeCreationButton = (Button)findViewById(R.id.route_creation_button);
		routeCreationButton.setOnClickListener(getOnClickListener());
		
		routeModificationButton = (Button)findViewById(R.id.route_modification_button);
		routeModificationButton.setOnClickListener(getOnClickListener());
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
				if(v==routeActivationButton){
					startRouteActivation();
				}
				else if(v==driverTransactionButton){
					startDriverTransaction();
				}
				else if(v==routeCreationButton){
					startRouteCreation();
				}
				else if(v==routeModificationButton){
					startRouteModification();
				}
			}
			
		};
	}
	
	private void startRouteActivation(){
		Intent intent = new Intent(this, RouteActivation.class);
		startActivity(intent);
	}
	
	private void startDriverTransaction(){
		Intent intent = new Intent(this, Transaction.class);
		startActivity(intent);
	}
	
	private void startRouteCreation(){
		Intent intent = new Intent(this, RouteCreation.class);
		startActivity(intent);
	}


	private void startRouteModification(){
		Intent intent = new Intent(this, RouteModification.class);
		startActivity(intent);
	}	
 	
	@Override
	protected void showMainMenu() {
		//nothing to do
	}

}
