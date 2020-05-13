package tom.sros.sorter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.storageRoom.BinDataBase;
import tom.sros.item.ItemDatabase;

public class newAlgorithm {
    
    /**
     * Finds the information related to the box types
     * 
     * @param dataBaseName
     * @param IDAmountList
     * @return List of boxes with their dimensions
     */
   public List<Box> asignBoxInformation(String dataBaseName, List<Box> IDAmountList){       
       List<Box> Boxes = new ArrayList<>();
       List<Box> returnBoxes = new ArrayList<>();
           
       //Creating a list ofn boxes with no information
       IDAmountList.forEach((currentBox) -> {
          for(int i = 0 ; i < (int)currentBox.getAmount(); i++){
              Boxes.add(new Box((String) currentBox.getID()));
           }
        });
           
       ItemDatabase IDB = new ItemDatabase();
       Boxes.forEach((currentBox) -> {
           //Assigning the width, length and height to the list of boxes
           returnBoxes.add(IDB.getBoxTypeInformation(currentBox.getID(), dataBaseName));
       });
        return returnBoxes;
   }
   
   /**
    * Works with the binary tree to sort boxes. Decides which box should try to be sorted
    * in which bin and the order of the sort.
    * 
    * @param dataBaseName
    * @param IDAmountList
    * @param removedBins 
    */
    public void sortAndAddToDB(String dataBaseName, List<Box> IDAmountList, List<Bin> removedBins){
    
    List<Box> freshSortBoxes;

    //obtaining bin information
    List<Bin> binsAvailable = BinDataBase.getBinInfo(dataBaseName);
    //obtaining box information
    List<Box> boxesAvailable = asignBoxInformation(dataBaseName, IDAmountList);
    List<Box> boxAvailableSorted = sortBySize(boxesAvailable);
    
    //Removing any bins from the algorithm that have been specified (if any have been specified)
    if(removedBins != null){
        removedBins.forEach((currentBinRemove) -> {
            for(Bin currentBinAvailable: binsAvailable){
                if(currentBinRemove.getName().equals(currentBinAvailable.getName())){
                    binsAvailable.remove(currentBinRemove);
                }
            }
        });
    }
    
        while(!boxAvailableSorted.isEmpty() && !binsAvailable.isEmpty()){
            List<Bin> largestBins = new ArrayList<>();
            //obtaining a list of the tallest bins available
            binsAvailable.forEach((currentBin) -> {
                if (largestBins.isEmpty()){
                    largestBins.add(currentBin);
                }
                else if (largestBins.get(0).getArea().getArea() < currentBin.getArea().getArea()){
                    largestBins.clear();
                    largestBins.add(currentBin);
                }
                else if(largestBins.get(0).getArea().getArea() == currentBin.getArea().getArea())
                    largestBins.add(currentBin);
            });

            //Sort the boxes available in to the curent talest bin
            ItemDatabase IDB = new ItemDatabase();
            for (Bin currentBin: largestBins){
                
                freshSortBoxes = binaryTree.sort2DBP(currentBin, boxAvailableSorted, dataBaseName);
                
                for(Box currentBox: freshSortBoxes){
                   //Remove any boxes that have been sorted from the unsorted list
                   if(IDB.blockRepeatingBoxEntry(currentBox, dataBaseName)){
                   IDB.addBoxLocation(currentBox, dataBaseName);
                   }
                   boxAvailableSorted.remove(currentBox);
                }
                
                binsAvailable.remove(currentBin);
            }
        }
        
        //Used if there are unsorted boxes once the algorithm is completed. Places the bins in the unsorted table
        if(!boxAvailableSorted.isEmpty()){
            JFrame unsortedWarning = new JFrame();
            JOptionPane.showMessageDialog(unsortedWarning, "Not all the boxes were able to fit in the bins provided. The unsorted boxes have been added to the unsorted boxes tab in storage room managment.", "Unsorted boxes", 2);
            
            ItemDatabase ITDB = new ItemDatabase();
            ITDB.addUnsorted(boxAvailableSorted, dataBaseName);
        }
    }
    
    private static List<Box> sortBySize(List<Box> boxesAvaialble){
        List<Box> tempList = boxesAvaialble;
        List<Box> returnList = new ArrayList<>();
        
        for(Box currentBox : tempList){
            System.out.println("Initial list box Width: " + currentBox.getWidth() + " Length: " + currentBox.getLength());
        }
        
        while(!tempList.isEmpty()){
            
            List<Box> largestBoxes = new ArrayList<>();
            
            //Finding the boxes that take the most surface area out of the current tallest boxes
            tempList.forEach((currentBox) -> {
                if (largestBoxes.isEmpty()){
                    largestBoxes.add(currentBox);
                }
                else if (largestBoxes.get(0).getArea().getArea() < currentBox.getArea().getArea()){
                    largestBoxes.clear();
                    largestBoxes.add(currentBox);
                }
                else if(largestBoxes.get(0).getArea().getArea() == currentBox.getArea().getArea())
                    largestBoxes.add(currentBox);
            });
            
            
            
            returnList.addAll(largestBoxes);
            tempList.removeAll(largestBoxes);
        }
        for(Box currentBox : returnList){
                System.out.println("fianl list box Width: " + currentBox.getWidth() + " Length: " + currentBox.getLength());
            }
        return returnList;
    }
}
