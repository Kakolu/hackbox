<!-- Build the HTML page with values from the call response -->
<html>
	<head>
		<title>eBay Search</title>
		<meta http-equiv="Content-type" content="text/html; charset=ISO-8859-1">
		<style type="text/css">
		td, tr {
			vertical-align: top; 
			border-bottom: solid 1px gray; }
		.title {
			position: relative;
			width: 700px;
			height: 100px;
			left: 300px;}
		.result_table{
			border: 1px solid black;
			border-collapse: collapse;}
		</style>
		<script type="text/javascript">
			 function test_key()
			 { 
				var keywd=document.getElementById("key").value;
				if(keywd==""||keywd==null)
				{
					alert("Please enter value for Key Words!!!")
					return false;
				}
				var min=parseInt(document.getElementById("from").value);
				var max=parseInt(document.getElementById("to").value);
				if((min!="" && max !="" && min > max)||(min!="" && max !="" && min<0)||(min!="" && max !="" && max<0))
				{
					alert("The Price range is not valid!!!")
					return false;
				}
				var day=document.getElementById("days").value;
				if(day==""|| day==null)
				{
					return true;
				}
				else if(day<1)
				{
					alert("Please enter valid number of handling days!!!")
					return false;
				}
				return true;
			 }
			 function clear_fields()
			 {				
				document.getElementById("sort").value="BestMatch";
				document.getElementById("list").value="5";	
				document.getElementById("result_content").innerHTML="";
				document.getElementById("new").checked = false;	
				document.getElementById("used").checked = false;	
				document.getElementById("vgood").checked = false;	
				document.getElementById("good").checked = false;	
				document.getElementById("accept").checked = false;	
				document.getElementById("now").checked = false;	
				document.getElementById("auc").checked = false;	
				document.getElementById("class").checked = false;	
				document.getElementById("return").checked = false;	
				document.getElementById("free").checked = false;	
				document.getElementById("exp").checked = false;					
			 }
		</script>
	</head>
	<body>
	<div class="title">
		<img src="http://cs-server.usc.edu:45678/hw/hw6/ebay.jpg" alt="ebay" width="130px" height="50px"style="vertical-align:middle;" />
		<h2 style="display:inline-block;vertical-align:middle;">Shopping</h2>
	</div>
	<form method="post" action="" onsubmit="return test_key()" >
		<fieldset style="width:780px; height:300px;"> <legend accesskey=I>Search Options</legend>
			<table style="width:100%">
				<tr> 
					<td>Key Words*:</td>
					<td> <input type="search" name="key" id="key" size="60"> </td> 
				</tr>
				<tr> 
					<td>Price Range:</td>
					<td> from $<input type="number" name="from" id="from" size="5"> to $<input type="number" name="to" id="to" size="5"> </td> 
				</tr>
				<tr> 
					<td>Condition:</td>
					<td> <input type="checkbox" name="new" id="new" value="1000">New &nbsp;<input type="checkbox" name="used" id="used" value="3000">Used&nbsp;<input type="checkbox" name="vgood" id="vgood" value="4000">Very Good&nbsp;<input type="checkbox" name="good" id="good" value="5000">Good&nbsp;<input type="checkbox" name="accept" id="accept" value="6000" >Acceptable </td> 
				</tr>
				<tr> 
					<td>Buying Formats:</td>
					<td> <input type="checkbox" name="now" id="now" value="FixedPrice">Buy It Now&nbsp;<input type="checkbox" name="auc" id="auc" value="Auction">Auction&nbsp;<input type="checkbox" name="class" id="class" value="Classified">Classified Ads</td> 
				</tr>
				<tr> 
					<td>Seller: </td>
					<td> <input type="checkbox" name="return" id="return" value="true">Return accepted<br/></td> 
				</tr>
				<tr> 
					<td> Shipping:</td>
					<td> <input type="checkbox" name="free" id="free" value="true">Free Shipping<br/><input type="checkbox" name="exp" id="exp" value="Expedited" >Expedited shipping available<br/>Max handling time(days):<input type="number" name="days" id="days" size="5"></td> 
				</tr>
				<tr> 
					<td>Sort by:</td>
					<td> <select name="sort" id="sort">
					<option value="BestMatch" selected="selected" > Best Match </option>
					<option value="CurrentPriceHighest"> Price: highest first </option>
					<option value="PricePlusShippingHighest"> Price + Shipping: highest first </option>
					<option value="PricePlusShippingLowest"> Price + Shipping: lowest first </option>
					</select></td> 
				</tr>
				<tr> 				
					<td>Results Per Page:</td>
					<td> <select name="list" id="list">
					<option selected="selected"> 5 </option>
					<option> 10 </option>
					<option> 15 </option>
					<option> 20 </option>
					</select></td> 
				</tr>
			</table>
			<div align="center">
				<input type="reset" value="Clear" onclick="clear_fields();">&nbsp;&nbsp;<input type="submit" name="submit" value="Search"> 
			</div>
		</fieldset>
	</form>
	<script type="text/javascript">

		//preserving values
			document.getElementById("key").value = "<?php echo $_POST["key"];?>";		 
			document.getElementById("from").value = "<?php echo $_POST["from"];?>";
			document.getElementById("to").value = "<?php echo $_POST["to"];?>";
			document.getElementById("days").value = "<?php echo $_POST["days"];?>";
			document.getElementById("sort").value = "<?php echo $_POST["sort"];?>";
			document.getElementById("list").value = "<?php echo $_POST["list"];?>";
			testvar = "<?php if( isset($_POST["new"]) && $_POST["new"] == 1000 ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("new").checked = true;
			}
			testvar = "<?php if(isset($_POST["used"]) &&  $_POST["used"] == 3000 ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("used").checked = true;
			}
			testvar = "<?php if( isset($_POST["vgood"]) && $_POST["vgood"] == 4000 ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("vgood").checked = true;
			}
			testvar = "<?php if( isset($_POST["good"]) && $_POST["good"] == 5000 ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("good").checked = true;
			}
			testvar = "<?php if( isset($_POST["accept"]) && $_POST["accept"] == 6000 ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("accept").checked = true;
			}
			testvar = "<?php if( isset($_POST["now"]) && $_POST["now"] == "FixedPrice" ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("now").checked = true;
			}
			testvar = "<?php if( isset($_POST["auc"]) && $_POST["auc"] == "Auction" ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("auc").checked = true;
			}
			testvar = "<?php if( isset($_POST["class"]) && $_POST["class"] == "Classified" ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("class").checked = true;
			}
			testvar = "<?php if( isset($_POST["return"]) && $_POST["return"] == "true" ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("return").checked = true;
			}
			testvar = "<?php if( isset($_POST["free"]) && $_POST["free"] == "true" ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("free").checked = true;
			}
			testvar = "<?php if( isset($_POST["exp"]) && $_POST["exp"] == "Expedited" ) {echo 'true';} else {echo '';}?>";
			if( testvar )
			{
				document.getElementById("exp").checked = true;
			}	
	 </script>
	<?php if(!empty($_POST["key"]) && !empty($_POST["submit"]))
	{

		$endpoint = 'http://svcs.ebay.com/services/search/FindingService/v1';  // URL to call
		$version = '1.0.0';  // API version supported by your application
		$appid = 'Universi-1724-49b3-9642-e932f5ee881b';  // Replace with your own AppID
		$globalid = 'EBAY-US';  // Global ID of the eBay site you want to search (e.g., EBAY-DE)
		$query = $_POST["key"];  // You may want to supply your own query
		$safequery = urlencode($query);  // Make the query URL-friendly
		$i = '0';  // Initialize the item filter index to 0
		
		// Create a PHP array of the item filters you want to use in your request
		$urlfilter = ""; 
		$i=0;  
		if($_POST["sort"])
		{
			$urlfilter .= "&itemFilter($i).name=sortOrder";
			$urlfilter .= "&itemFilter($i).value=".$_POST["sort"];
			$i++;
		}
		if($_POST["from"])
		{
			$urlfilter .= "&itemFilter($i).name=MinPrice";
			$urlfilter .= "&itemFilter($i).value=".$_POST["from"];
			$i++;
		}
		if($_POST["to"])
		{
			$urlfilter .= "&itemFilter($i).name=MaxPrice";
			$urlfilter .= "&itemFilter($i).value=".$_POST["to"];
			$i++;
		}
		if(!empty($_POST["new"])||!empty($_POST["used"])||!empty($_POST["vgood"])||!empty($_POST["good"])||!empty($_POST["accept"]))
		{
			$urlfilter .= "&itemFilter($i).name=Condition";
			if(!empty($_POST["new"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["new"];
			}
			if(!empty($_POST["used"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["used"];
			}
			if(!empty($_POST["vgood"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["vgood"];
			}
			if(!empty($_POST["good"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["good"];
			}
			if(!empty($_POST["accept"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["accept"];
			}
			$i++;
		}
		if(!empty($_POST["now"])||!empty($_POST["auc"])||!empty($_POST["class"]))
		{
			$urlfilter .= "&itemFilter($i).name=ListingType";
			if(!empty($_POST["now"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["now"];
			}
			if(!empty($_POST["auc"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["auc"];
			}
			if(!empty($_POST["class"]))
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["class"];
			}
			$i++;
		}
		if(!empty($_POST["return"]))
		{
			$urlfilter .= "&itemFilter($i).name=ReturnsAcceptedOnly";
			$urlfilter .= "&itemFilter($i).value=".$_POST["return"];
			$i++;
		}
		if(!empty($_POST["free"]))
		{
			$urlfilter .= "&itemFilter($i).name=FreeShippingOnly";
			$urlfilter .= "&itemFilter($i).value=".$_POST["free"];
			$i++;
		}
		if(!empty($_POST["exp"]))
		{
			$urlfilter .= "&itemFilter($i).name=ExpeditedShippingType";
			$urlfilter .= "&itemFilter($i).value=".$_POST["exp"];
			$i++;
		}
		if($_POST["days"])
		{
			$urlfilter .= "&itemFilter($i).name=MaxHandlingTime";
			$urlfilter .= "&itemFilter($i).value=".$_POST["days"];
			$i++;
		}

		// Construct the findItemsByKeywords HTTP GET call 
		$apicall = "$endpoint?";
		$apicall .= "OPERATION-NAME=findItemsByKeywords";
		$apicall .= "&SERVICE-VERSION=$version";
		$apicall .= "&SECURITY-APPNAME=$appid";
		$apicall .= "&GLOBAL-ID=$globalid";
		$apicall .= "&keywords=$safequery";
		$apicall .= "&paginationInput.entriesPerPage=".$_POST['list'];
		$apicall .= "$urlfilter";
		
		// Load the call and capture the document returned by eBay API
		$resp = simplexml_load_file($apicall);
		$top = "http://cs-server.usc.edu:45678/hw/hw6/itemTopRated.jpg";
		
		// Check to see if the request was successful, else print an error
		if ($resp->ack == "Success") 
		{
		  $results = "";
		  $total = $resp->paginationOutput->totalEntries;
		  // If the response was loaded, parse it and build links  
		  foreach($resp->searchResult->item as $item) 
		  {
			$pic   = $item->galleryURL;
			$link  = $item->viewItemURL;
			$title = $item->title;
			//Condition
			if($item->condition->conditionId == "1000" )
			$item_condition = "<br/><b>Condition: </b>New<img src=\"$top\" width=\"30px\" height=\"30px\"><br/><br/>";
			else if($item->condition->conditionId == "3000" )
				$item_condition = "<br/><b>Condition: </b>Used<br/><br/>";
			else if($item->condition->conditionId == "4000" )
				$item_condition = "<br/><b>Condition: </b>Very Good<br/><br/>";
			else if($item->condition->conditionId == "5000" )
				$item_condition = "<br/><b>Condition: </b>Good<br/><br/>";
			else if($item->condition->conditionId == "6000" )
				$item_condition = "<br/><b>Condition: </b>Acceptable<br/><br/>";
			else
				$item_condition = "<br/><b>Condition: </b>".$item->condition->conditionDisplayName."<br/><br/>";
			//Listing type
			if($item->listingInfo->listingType == "FixedPrice" || $item->listingInfo->listingType == "StoreInventory")
			$item_list = "<b>Buy It Now</b><br/>";
			else if($item->listingInfo->listingType == "Auction")
			$item_list = "<b>Auction</b><br/>";
			else if($item->listingInfo->listingType == "Classified")
			$item_list = "<b>Classified Ad</b><br/>";
			else
			$item_list = "<b>".$item->listingInfo->listingType."</b><br/>";
			//Returns
			if($item->returnsAccepted == true)
			$item_returns = "Seller Accepts Returns";
			else
				$item_returns = "No Returns Accepted";
			//Shipping cost
			if($item->shippingInfo->shippingServiceCost == 0.0)
				$item_freeship = "Free Shipping";
			else
				$item_freeship = "Shipping not free";
			//Expedited
			if($item->shippingInfo->expeditedShipping == true)
			$item_exp = "-- Expedited Shipping Available";
			else
				$item_exp = "-- Expedited Shipping Not Available";
			//Handling time
			if(($item->shippingInfo->handlingTime!="") &&($item->shippingInfo->handlingTime != null))
			$item_handling = "-- Handled for shipping in ".$item->shippingInfo->handlingTime . " day(s).";
			else
				$item_handling = "";
			if($item->shippingInfo->shippingServiceCost > 0.0)
			$price = "<b>Price: $".$item->sellingStatus-> convertedCurrentPrice ."(+$".$item->shippingInfo->shippingServiceCost." for shipping)</b>";	
			else
				$price = "<b>Price: $".$item->sellingStatus-> convertedCurrentPrice . "</b>";
			//Location
			$location = "<i>From ".$item->location. "</i>";
			
			// For each SearchResultItem node, build a link and append it to $results
			$results .= "<tr><td><img src=\"$pic\" width=\"210px\" height=\"210px\"></td><td><a href=\"$link\">$title</a>$item_condition $item_list<br/>$item_returns<br/>$item_freeship $item_exp $item_handling<br/><br/><br/>$price  $location </td></tr>";
		  }
	    ?>
		<div id="result_content">
		<?php if($total>=1)
			{?>
		
			<h2 style="margin-left: 250px;"><?php echo $total?> eBay Results for <i><?php echo $query?></i></h2>
			<table class="result_table" >
			<tr>
			  <td>
				<?php echo $results; ?>
			  </td>
			</tr>
			</table>
		<?php
		  }
			else
			{
				$results="No Results Found!";
				 echo "<h1 style=\"margin-left: 250px;\">".$results."</h1>";
			}
		}
		// If the response does not indicate 'Success,' print an error
		else {
		  $results  = "<h3 style=\"margin-left: 250px;\">Oops! The request was not successful.</h3>";
		echo $results;
		}	
	}	?>
	</div>



	<!--<p><b>API request used (click URL to view XML response):</b></p>
	<p><a href="<?php echo $apicall;?>"><?php echo $apicall;?></a></p>-->
	</body>
</html>