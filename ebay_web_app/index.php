
<?php 
	header('Access-Control-Allow-Origin: *');
	header("Access-Control-Allow-Methods: GET , POST");
	header('Content-type: application/json');
	
	if(!empty($_POST["key"]))
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
		if($_POST["from"] && ($_POST["from"])!= null)
		{
			$urlfilter .= "&itemFilter($i).name=MinPrice";
			$urlfilter .= "&itemFilter($i).value=".$_POST["from"];
			$i++;
		}
		if($_POST["to"] && ($_POST["to"])!= null)
		{
			$urlfilter .= "&itemFilter($i).name=MaxPrice";
			$urlfilter .= "&itemFilter($i).value=".$_POST["to"];
			$i++;
		}
		if(!empty($_POST["newp"])||!empty($_POST["used"])||!empty($_POST["vgood"])||!empty($_POST["good"])||!empty($_POST["accept"]))
		{
			$urlfilter .= "&itemFilter($i).name=Condition";
			if(!empty($_POST["newp"]) && ($_POST["newp"])!=0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["newp"];
			}
			if(!empty($_POST["used"]) && ($_POST["used"])!=0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["used"];
			}
			if(!empty($_POST["vgood"]) && ($_POST["vgood"])!=0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["vgood"];
			}
			if(!empty($_POST["good"]) && ($_POST["good"])!=0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["good"];
			}
			if(!empty($_POST["accept"]) && ($_POST["accept"])!=0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["accept"];
			}
			$i++;
		}
		if(!empty($_POST["now"])||!empty($_POST["auc"])||!empty($_POST["classified"]))
		{
			$urlfilter .= "&itemFilter($i).name=ListingType";
			if(!empty($_POST["now"]) && !($_POST["now"])==0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["now"];
			}
			if(!empty($_POST["auc"]) && !($_POST["auc"])==0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["auc"];
			}
			if(!empty($_POST["classified"]) && !($_POST["classified"])==0)
			{
				$urlfilter .= "&itemFilter($i).value=".$_POST["classified"];
			}
			$i++;
		}
		if(!empty($_POST["returnp"]) && ($_POST["returnp"])!=0)
		{
			$urlfilter .= "&itemFilter($i).name=ReturnsAcceptedOnly";
			$urlfilter .= "&itemFilter($i).value=".$_POST["returnp"];
			$i++;
		}
		if(!empty($_POST["free"]) && ($_POST["free"])!=0)
		{
			$urlfilter .= "&itemFilter($i).name=FreeShippingOnly";
			$urlfilter .= "&itemFilter($i).value=".$_POST["free"];
			$i++;
		}
		if(!empty($_POST["exp"]) && ($_POST["exp"])!=0)
		{
			$urlfilter .= "&itemFilter($i).name=ExpeditedShippingType";
			$urlfilter .= "&itemFilter($i).value=".$_POST["exp"];
			$i++;
		}
		if($_POST["days"] && ($_POST["days"])!=null)
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
		$apicall .= "&paginationInput.entriesPerPage=".$_POST['list']."&paginationInput.pageNumber=".$_POST['pageno'];
		$apicall .= "$urlfilter&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo&outputSelector(2)=PictureURLSuperSize";
		
		// Load the call and capture the document returned by eBay API
		$resp = simplexml_load_file($apicall);
		$top = "http://cs-server.usc.edu:45678/hw/hw6/itemTopRated.jpg";
		
		// Check to see if the request was successful, else print an error
		
		if ($resp->ack == "Success") 
		{
			$total = $resp->paginationOutput->totalEntries;
			$item=$resp->searchResult->item;
			$ipage = $resp->paginationOutput->entriesPerPage;
			$ItemsList = array(
			"ack" => $resp->ack,
			"jsonValid" => "true",
			"query" => $query,
			"resultCount" => $resp->paginationOutput->totalEntries,
			"pageNumber" => $resp->paginationOutput->pageNumber,
			"itemCount" => $resp->paginationOutput->entriesPerPage,
			"request" => $apicall
			
			);
			$i=0;
			 foreach($resp->searchResult->item as $item) 
			{
				$itemX = "item".$i;
				$ItemsList[$itemX] = array(
					"basicInfo" => array(
						"title" => $item->title,
						"viewItemURL" => $item->viewItemURL,
						"galleryURL" => $item->galleryURL,
						"pictureURLSuperSize" => $item->pictureURLSuperSize,
						"convertedCurrentPrice" => $item->sellingStatus->convertedCurrentPrice,
						"shippingServiceCost" => $item->shippingInfo->shippingServiceCost,
						"conditionDisplayName" => $item->condition->conditionDisplayName,
						"listingType" => $item->listingInfo->listingType,
						"location" => $item->location,
						"categoryName" => $item->primaryCategory->categoryName,
						"topRatedListing" => $item->topRatedListing
					),
				"sellerInfo" => array(
					"sellerUserName" => $item->sellerInfo->sellerUserName,
					"feedbackScore" => $item->sellerInfo->feedbackScore,
					"positiveFeedbackPercent" => $item->sellerInfo->positiveFeedbackPercent,
					"feedbackRatingStar" => $item->sellerInfo->feedbackRatingStar,
					"topRatedSeller" => $item->sellerInfo->topRatedSeller,
					"sellerStoreName" => $item->storeInfo->storeName,
					"sellerStoreURL" => $item->storeInfo->storeURL
				),
				"shippingInfo" => array(
					"shippingType" => $item->shippingInfo->shippingType,
					"shipToLocations" => $item->shippingInfo->shipToLocations,
					"expeditedShipping" => $item->shippingInfo->expeditedShipping,
					"oneDayShippingAvailable" => $item->shippingInfo->oneDayShippingAvailable,
					"returnsAccepted" => $item->returnsAccepted,
					"handlingTime" => $item->shippingInfo->handlingTime
				));
				$i++;
			}
			echo json_encode($ItemsList);
			
		}
		else
		{
			echo json_encode( array( 
			"jsonValid" => "false",
			"request" => $apicall
			));
			
		}
	}
?>
