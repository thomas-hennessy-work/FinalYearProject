package tom.sros.sorter;

import java.util.ArrayList;
import java.util.List;
import tom.sros.storageRoom.BinDataBase;
import tom.sros.item.ItemDatabase;

public class newAlgorithm {
    
   //Given a list of pairs containing ID's of boxes and amounts. Produces a list
   //of boxes, including multiples of the same box (as defined by amount) along with
   //their dimensions
   public List<Box> asignBoxInformation(String dataBaseName, List<Box> IDAmountList){       
       List<Box> Boxes = new ArrayList<>();
       List<Box> returnBoxes = new ArrayList<>();
           
       IDAmountList.forEach((currentBox) -> {
          for(int i = 0 ; i < (int)currentBox.getAmount(); i++){
              Boxes.add(new Box((String) currentBox.getID()));
           }
        });
           
        ItemDatabase IDB = new ItemDatabase();
       //Seperated to avoid repeatedly calling the DB
       Boxes.forEach((currentBox) -> {
           returnBoxes.add(IDB.getBoxInformation(currentBox.getID(), dataBaseName));
       });

        return returnBoxes;
   }
   
    public void sortAndAddToDB(String dataBaseName, List<Box> IDAmountList){
    
    List<Box> returnBoxList = new ArrayList<>();
    List<Box> freshSortBoxes;

    //obtaining bin information
    List<Bin> binsAvailable = BinDataBase.getBinInfo(dataBaseName);
    //obtaining box information
    List<Box> boxesAvailable = asignBoxInformation(dataBaseName, IDAmountList);
            
        while(!boxesAvailable.isEmpty() && !binsAvailable.isEmpty()){
            List<Bin> tallestBins = new ArrayList<>();
            //obtaining a list of the tallest bins available
            binsAvailable.forEach((currentBin) -> {
                if (tallestBins.isEmpty()){
                    tallestBins.add(currentBin);
                }
                else if (tallestBins.get(0).getHeight() < currentBin.getHeight()){
                    tallestBins.clear();
                    tallestBins.add(currentBin);
                }
                else if(tallestBins.get(0).getHeight() == currentBin.getHeight())
                    tallestBins.add(currentBin);
            });

            //Sort the boxes available in to the curent talest bin
            ItemDatabase IDB = new ItemDatabase();
            for (Bin currentBin: tallestBins){
                freshSortBoxes = binaryTree.sort2DBP(currentBin, boxesAvailable, dataBaseName);
                System.out.println("Size of freshSortBoxes = " + freshSortBoxes.size());
                for(Box currentBox: freshSortBoxes){
                   //Remove any boxes that have been sorted from the unsorted list
                   if(IDB.blockRepeatingBoxEntry(currentBox, dataBaseName)){
                    IDB.addBoxLocation(currentBox, dataBaseName);
                    returnBoxList.add(currentBox);
                   }
                   boxesAvailable.remove(currentBox);
                }
                binsAvailable.remove(currentBin);
            }
    }
        System.out.println(returnBoxList.toString());
    }
}
