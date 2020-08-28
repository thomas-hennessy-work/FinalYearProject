package tom.sros.sorter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import tom.sros.storageRoom.BinDataBase;
import tom.sros.item.ItemDatabase;

public class binaryTreeAlgorithm {
    
    /**
     * Finds the information related to the box types
     * 
     * @param dataBaseName
     * @param IDAmountList
     * @return List of boxes with their dimensions
     */
   public List<BoxIndividual> asignBoxInformation(String dataBaseName, List<BoxType> IDAmountList){       
       List<BoxIndividual> Boxes = new ArrayList<>();
       List<BoxIndividual> returnBoxes = new ArrayList<>();
           
       //Creating a list ofn boxes with no information
       IDAmountList.forEach((currentBox) -> {
          for(int i = 0 ; i < (int)currentBox.getAmount(); i++){
              Boxes.add(new BoxIndividual((String) currentBox.getID()));
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
    public void sortAndAddToDB(String dataBaseName, List<BoxType> IDAmountList, List<Bin> removedBins){
    
        List<BoxIndividual> freshSortBoxes;

        //obtaining bin information
        List<Bin> binsAvailable = BinDataBase.getBinInfo(dataBaseName);
        //obtaining box information
        List<BoxIndividual> boxesAvailable = asignBoxInformation(dataBaseName, IDAmountList);
        List<BoxIndividual> boxAvailableSorted = sortBySize(boxesAvailable);

        if(removedBins != null){
            for(int i=0 ; i < binsAvailable.size() ; i ++){
                for(Bin currentRemovedBin : removedBins){
                    if(currentRemovedBin.getName().equals(binsAvailable.get(i).getName())){
                        binsAvailable.remove(i);
                    }
                }
            }
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
                
                for(BoxIndividual currentBox: freshSortBoxes){
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
    
    private static List<BoxIndividual> sortBySize(List<BoxIndividual> boxesAvaialble){
        List<BoxIndividual> tempList = boxesAvaialble;
        List<BoxIndividual> returnList = new ArrayList<>();
        
        for(BoxType currentBox : tempList){
            System.out.println("Initial list box Width: " + currentBox.getWidth() + " Length: " + currentBox.getLength());
        }
        
        while(!tempList.isEmpty()){
            
            List<BoxIndividual> largestBoxes = new ArrayList<>();
            
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
        returnList.forEach((currentBox) -> {
            System.out.println("fianl list box Width: " + currentBox.getWidth() + " Length: " + currentBox.getLength());
       });
        return returnList;
    }
}
