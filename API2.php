<?php
$response = array(); //data buffer

if(isset($_GET['apicall']))
{
	switch($_GET['apicall'])
	{

		case 'AOD':
 			//Jatin pls
			break;
		case 'waterVapor':
	  		$response['waterVapor']='waterVapor';
	  		break;
  		case 'temp':
			$resultarr=array();
			$providedLat=(float)$_POST['Lat']; //Get values from the app here
			$providedLong=(float)$_POST['Long'];

			$rows = file('AQI/Testmod.csv');
			$last_row = array_pop($rows);
			$data = str_getcsv($last_row);
			$providedDate=$data[0]; //IMPORTANT NOTE: KEEP DATE IN FIRST COLUMN IN THE DATASET

			$file = fopen("AQI/Testmod.csv","r"); //For Fetching data
			$temp=fgetcsv($file);
			array_push($resultarr,$temp);
			$date=array_search('Date Local',$temp);
			$lat=array_search('Latitude',$temp);
			$long=array_search('Longitude',$temp);
			while(!feof($file))
			{
				$temp=fgetcsv($file); // fetch rows into temp array
				if($temp[$lat]>=($providedLat-5) && $temp[$lat]<=($providedLat+5) && $temp[$long]>=($providedLong-5) && $temp[$long]<=($providedLong+5) && $temp[$date]==$providedDate)//check for req row or discard
				{
					array_push($resultarr,$temp);
				}
				
			}
			fclose($file);
			$response['error'] = false;
			$response['message'] = 'Local Data only - retrived!';
			$response['resultset'] = $resultarr;
			break;
			break;

		case 'vis':
			$resultarr=array();
			$providedLat=(float)$_POST['Lat']; //Get values from the app here
			$providedLong=(float)$_POST['Long'];

			$rows = file('VIS/Testmod.csv');
			$last_row = array_pop($rows);
			$data = str_getcsv($last_row);
			$providedDate=$data[0]; //IMPORTANT NOTE: KEEP DATE IN FIRST COLUMN IN THE DATASET

			$file = fopen("AQI/Testmod.csv","r"); //For Fetching data
			$temp=fgetcsv($file);
			array_push($resultarr,$temp);
			$date=array_search('Date Local',$temp);
			$lat=array_search('Latitude',$temp);
			$long=array_search('Longitude',$temp);
			while(!feof($file))
			{
				$temp=fgetcsv($file); // fetch rows into temp array
				if($temp[$lat]>=($providedLat-5) && $temp[$lat]<=($providedLat+5) && $temp[$long]>=($providedLong-5) && $temp[$long]<=($providedLong+5) && $temp[$date]==$providedDate)//check for req row or discard
				{
					array_push($resultarr,$temp);
				}
				
			}
			fclose($file);
			$response['error'] = false;
			$response['message'] = 'Local Data only - retrived!';
			$response['resultset'] = $resultarr;
			break;
		case 'loc':
			$resultarr=array();
			$providedLat=(float)$_POST['Lat']; //Get values from the app here
			$providedLong=(float)$_POST['Long'];

			$rows = file('AQI/Testmod.csv');
			$last_row = array_pop($rows);
			$data = str_getcsv($last_row);
			$providedDate=$data[0]; //IMPORTANT NOTE: KEEP DATE IN FIRST COLUMN IN THE DATASET

			$file = fopen("AQI/Testmod.csv","r"); //For Fetching data
			$temp=fgetcsv($file);
			array_push($resultarr,$temp);
			$date=array_search('Date Local',$temp);
			$lat=array_search('Latitude',$temp);
			$long=array_search('Longitude',$temp);
			while(!feof($file))
			{
				$temp=fgetcsv($file); // fetch rows into temp array
				if($temp[$lat]>=($providedLat-5) && $temp[$lat]<=($providedLat+5) && $temp[$long]>=($providedLong-5) && $temp[$long]<=($providedLong+5) && $temp[$date]==$providedDate)//check for req row or discard
				{
					array_push($resultarr,$temp);
				}
				
			}
			fclose($file);
			$response['error'] = false;
			$response['message'] = 'Local Data only - retrived!';
			$response['resultset'] = $resultarr;
			break;
		case 'fetchall':
			
			$rows = file('AQI/Testmod.csv');
			$last_row = array_pop($rows);
			$data = str_getcsv($last_row);
			

			$file = fopen("testH.csv","r"); //For Fetching data
			$temp=array();
			$resultarr=array();
			while(!feof($file))
			{
				$temp=fgetcsv($file); // fetch rows into temp array
				if($data[0]==$temp[0]){
				array_push($resultarr,$temp);//collect all rows into $response
				}
			}
			fclose($file);
			$response['error'] = false;
			$response['message'] = 'All data retrived!';
			$response['resultset'] = $resultarr;
			break;	
		default:
			$response['error'] = true;
			$response['message'] = 'Invalid Operation Called';
	}
}
else
{
	$response['error'] = true;
	$response['message'] = 'Invalid API Call';
}
echo json_encode($response); //Send $response 2D array to client
?>