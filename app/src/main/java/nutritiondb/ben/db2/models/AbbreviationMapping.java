package nutritiondb.ben.db2.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benebsworth on 30/10/16.
 */
public class AbbreviationMapping {

    /**
     * Dictionary lookup containing the mapping between nutrient abbreviations standardized by
     * the universial INFOODS schema and the corresponding names for a food.
     */
    private static Map<String, String> nameMapping = new HashMap<>();
    private static Map<String, String> defaultUnitsMapping = new HashMap<>();



    static {
        HashMap<String, String> tmp = new HashMap<>();

        //Energy
        tmp.put("ENERC", "Energy");
        tmp.put("ENERC1", "Energy, including dietary fibre");
        tmp.put("ENERC_KCAL", "Energy");
        //Carbohydrates
        tmp.put("CHOCDF", "Carbohydrate, by difference");
        tmp.put("CHOAVL", "Available carbohydrate");
        tmp.put("SUGAR", "Sugar Total");
        tmp.put("FIBTG", "Dietary Fibre");
        tmp.put("SUCS", "Sucrose");
        tmp.put("MALS", "Maltose");
        tmp.put("GALS", "Galactose");
        tmp.put("FRUS", "Fructose");
        tmp.put("GLUS", "Glucose");
        tmp.put("STARCH", "Starch");
        //common
        tmp.put("DEXTN", "Dextrin");
        tmp.put("GYRL", "Glycerol");
        tmp.put("GLYC", "Glycogen");
        tmp.put("INULN", "Inulin");
        tmp.put("MANTL", "Mannitol");
        tmp.put("MALTDEX", "Maltodextrin");
        tmp.put("OLSAC", "Oligosaccharides");
        tmp.put("RAFS", "Raffinose");
        tmp.put("STAS", "Stachyose");
        tmp.put("SORTL", "Sorbitol");


        //Protiens & amino acids
        tmp.put("PROCNT", "Protein");
        tmp.put("TRP", "Tryptophan");
        tmp.put("TYR", "Tyrosine");
        tmp.put("THR", "Threonine");
        tmp.put("SER", "Serine");
        tmp.put("ALA", "Alanine");
        tmp.put("ARG", "Arginine");
        tmp.put("ASP", "Aspartic acid");
        tmp.put("CYS", "Cystine");
        tmp.put("HIS", "Histidine");
        tmp.put("ILE", "Isoleucine");
        tmp.put("HYP", "Hyroxyprofile");
        tmp.put("PRO", "Proline");
        tmp.put("GLU", "Glutamic acid");
        tmp.put("GLY", "Glycine");
        tmp.put("PHE", "Phenylalanine,");
        tmp.put("MET", "Methionine");
        tmp.put("LYS", "Lysine");
        tmp.put("LEU", "Leucine");
        //Fats & fatty acids
        tmp.put("FAT", "Total lipid (fat)");
        tmp.put("FAMS", "Fatty acids, total monounsaturated");
        tmp.put("FAPU", "Fatty acids, total polyunsaturated");
        tmp.put("FASAT", "Fatty acids, total saturated");
        tmp.put("FATRN", "Fatty acids, total trans");
        tmp.put("F4D0", "4:0");
        tmp.put("F6D0", "6:0");
        tmp.put("F8D0", "8:0");
        tmp.put("F10D0", "10:0");
        tmp.put("F10D1", "10:1");
        tmp.put("F12D0", "12:0");
        tmp.put("F13D0", "13:0");
        tmp.put("F14D0", "14:0");
        tmp.put("F14D1", "14:1");
        tmp.put("F15D0", "15:0");
        tmp.put("F15D1", "15:1");
        tmp.put("F16D0", "16:0");
        tmp.put("F16D1", "16:1 undifferentiated");
        tmp.put("F16D1T", "16:0T"); //trans
        tmp.put("F16D1T", "16:0C"); //cis
        tmp.put("F17D0", "17:0");
        tmp.put("F17D1", "17:1");
        tmp.put("F18D0", "18:0");
        tmp.put("F18D1C", "18:1 C");
        tmp.put("F18D1TN7", "18:1 T7");
        tmp.put("F18D1", "18:1 undifferentiated");
        tmp.put("F18D2", "18:2 undifferentiated");
        tmp.put("F18D2CN6", "18:2 c n-6");
        tmp.put("F18D2TT", "18:2 TT");
        tmp.put("F18D2I", "18:2 I");
        tmp.put("F18D2N6", "18:2 n-6");
        tmp.put("F18D3", "18:3 undifferentiated");
        tmp.put("F18D3T", "18:3 T");
        tmp.put("F18D3I", "18:3 I");
        tmp.put("F18D3N6", "18:3 n-6");
        tmp.put("F18D3N3F", "18:3 n-3"); // per unit
        tmp.put("F18D3N3", "18:3 n-3"); // per %
        tmp.put("F18D4", "18:4");
        tmp.put("F18D4N3", "18:4 n-3");
        tmp.put("F19D0", "19:0");
        tmp.put("F20D0", "20:0");
        tmp.put("F20D1", "20:1");
        tmp.put("F20D1N11F", "20:1 n-11");
        tmp.put("F20D2N6", "20:2 n-6 c,c");
        tmp.put("F20D3N3", "20:2 n-3");
        tmp.put("F20D3", "20:3 undifferentiated");
        tmp.put("F20D4", "20:4 undifferentiated");
        tmp.put("F20D4N6", "20:4 n-6");
        tmp.put("F20D5", "20:5 n-3 (EPA)");
        tmp.put("F21D0", "21:0");
        tmp.put("F22D0", "22:0");
        tmp.put("F22D1", "22:1 undifferentiated");
        tmp.put("F22D1C", "22:1 c");
        tmp.put("F22D1T", "22:1 t");
        tmp.put("F22D3", "20:2 n-3");
        tmp.put("F22D5N3", "22:5 n-3 (DPA)");
        tmp.put("F22D6", "22:6");
        tmp.put("F22D6N3", "22:6 n-3 (DHA)");
        tmp.put("F23D0", "23:0");
        tmp.put("F24D0", "24:0");
        tmp.put("F24D1", "24:1");
        tmp.put("F24D1C", "24:1 c");
        tmp.put("LCW3TOTAL", "Total Omega 3 Fatty Acid");
        tmp.put("FAUNDIFF", "Undifferentiated fatty acids");

        //acids
        tmp.put("ACEAC", "Acetic acid");
        tmp.put("CITAC", "Citric acid");
        tmp.put("FUMAC", "Fumaric acid");
        tmp.put("LACAC", "Lactic acid");
        tmp.put("MALAC", "Malic acid");
        tmp.put("OXALAC", "Oxalic acid");
        tmp.put("PROPAC", "Propionic acid");
        tmp.put("QUINAC", "Quinic acid");
        tmp.put("SHIKAC", "Shikimic acid");
        tmp.put("SUCAC", "Succinic acid");
        tmp.put("TARAC", "Tartaric acid");


//        tmp.put("", "");
        //Minerals & metals
        tmp.put("AL", "Aluminium");
        tmp.put("AS", "Arsenic");
        tmp.put("CA", "Calcium (Ca)");
        tmp.put("CD", "Cadmium (Cd)");
        tmp.put("CU", "Copper (Cu)");
        tmp.put("CO", "Cobalt (Co)");
        tmp.put("CR", "Chromium (Cr)");
        tmp.put("FD", "Fluoride (F)");
        tmp.put("FE", "Iron (Fe)");
        tmp.put("HG", "Mercury (Hg)");
        tmp.put("ID", "Iodine (I)");
        tmp.put("K", "Potassium (K)");
        tmp.put("MG", "Magnesium (Mg)");
        tmp.put("MN", "Manganese (Mn)");
        tmp.put("M0", "Molybdenum (Mo)");
        tmp.put("NA", "Sodium (Na)");
        tmp.put("NI", "Nickel (Ni)");
        tmp.put("P", "Phosphorus (P)");
        tmp.put("PB", "Lead (Pb)");
        tmp.put("S", "Sulphur (S)");
        tmp.put("SE", "Selenium (Se)");
        tmp.put("SN", "Tin (Sn)");
        tmp.put("ZN", "Zinc (Zn)");
        //Vitamins
        tmp.put("BIOT", "Biotin (B7)");
        tmp.put("B1", "Thiamin (B1)");
        tmp.put("B2", "Riboflavin (B2)");
        tmp.put("B3", "Niacin (B3)");
        tmp.put("VITB6", "Pyridoxine (B6)");
        tmp.put("VITB12", "Cobalamin (B12)");
        tmp.put("CARTA", "Alpha carotene");
        tmp.put("CARTB", "Beta carotene");
        tmp.put("CARTBEQ", "Beta carotene equivalents");
        tmp.put("CHOLN", "Choline");
        tmp.put("CRYPX", "Cryptoxanthin");
        tmp.put("NIA", "Niacin (B3)");
        tmp.put("NIAEQ", "Niacin Equivalents");

        tmp.put("FOL", "Total folates");
        tmp.put("FOLAC", "Folic acid");
        tmp.put("FOLFD", "Folate, natural");
        tmp.put("FOLDFE", "Dietary folate equivalents");
        tmp.put("LUT+ZEA", "Lutein + Zeaxanthin");
        tmp.put("LUTN", "Lutein");
        tmp.put("LYCPN", "Lycopene");

        tmp.put("MK4", "Menaquinone-4");
        tmp.put("PANTAC", "Pantothenic acid (B5)");
        tmp.put("VITB6A", "Pyridoxine (B6)");
        tmp.put("VITB12", "Cobalamin (B12)");
        tmp.put("VITE", "Vitamin E");
        tmp.put("VITK", "Vitamin K (phylloquinone)");
        tmp.put("VITK1D", "");
        tmp.put("RETOL", "Retinol");
        tmp.put("VITA_IU", "");
        tmp.put("VITA", "Retinol equivalents (VIT A)");
        tmp.put("VITC", "Vitamin C");
        //vit d
        tmp.put("CHOCAL", "Cholecalciferol (D3)");
        tmp.put("ERGCAL", "Ergocalciferol (D2)");
        tmp.put("CHOCALOH", "25-OH Cholecalciferol (25-OH D3)");
        tmp.put("ERGCALOH", "25-OH Ergocalciferol (25-OH D2)");
        tmp.put("VITDEQ", "Vitamin D3 equivalents, with factors");
        tmp.put("VITD", "Vitamin D");
        //sterols
        tmp.put("PHYSTR", "Phytosterols");
        tmp.put("STID7", "Stigmasterol"); //usually delta 7-stigmasterol
        tmp.put("CAMD5", "Campesterol");
        tmp.put("SITSTR", "Sitosterol");
        tmp.put("TOCPHA", "Alpha-tocopherol");
        tmp.put("TOCTRA", "Alpha-tocotrienol");
        tmp.put("TOCPHB", "Beta-tocopherol");
        tmp.put("TOCTRB", "Beta-tocotrienol");
        tmp.put("TOCPHD", "Delta-tocopherol");
        tmp.put("TOCTRD", "Delta-tocotrienol");
        tmp.put("TOCPHG", "Gamma-tocopherol");
        tmp.put("TOCTRG", "Gamma-tocotrienol");
        //Other
        tmp.put("CAFFN", "Caffeine");
        tmp.put("CHOLE", "Cholesterol");
        nameMapping = Collections.unmodifiableMap(tmp);

        tmp.clear();
        // ------- DEFAULT UNITS ------------
        // Units should be dynamic based on the size of the value relative to the default
        // so for example if you have 0.001g then convert to 1mg etc.
        //Energy
        tmp.put("ENERC", "kJ");
        tmp.put("ENERC1", "kJ");
        tmp.put("ENERC_KCAL", "kCal");
        //Carbohydrates
        tmp.put("CHOCDF", "g");
        tmp.put("CHOAVL", "g");
        tmp.put("SUGAR", "g");
        tmp.put("FIBTG", "g");
        tmp.put("SUCS", "g");
        tmp.put("MALS", "g");
        tmp.put("GALS", "g");
        tmp.put("FRUS", "g");
        tmp.put("GLUS", "g");
        tmp.put("STARCH", "g");
        //common
        tmp.put("DEXTN", "g");
        tmp.put("GYRL", "g");
        tmp.put("GLYC", "g");
        tmp.put("INULN", "g");
        tmp.put("MANTL", "g");
        tmp.put("MALTDEX", "g");
        tmp.put("OLSAC", "g");
        tmp.put("RAFS", "g");
        tmp.put("STAS", "g");
        tmp.put("SORTL", "g");


        //Protiens & amino acids
        tmp.put("PROCNT", "g");
        tmp.put("TRP", "mg");
        tmp.put("TYR", "mg");
        tmp.put("THR", "mg");
        tmp.put("SER", "mg");
        tmp.put("ALA", "mg");
        tmp.put("ARG", "mg");
        tmp.put("ASP", "mg");
        tmp.put("CYS", "mg");
        tmp.put("HIS", "mg");
        tmp.put("ILE", "mg");
        tmp.put("HYP", "mg");
        tmp.put("PRO", "mg");
        tmp.put("GLU", "mg");
        tmp.put("GLY", "mg");
        tmp.put("PHE", "mg,");
        tmp.put("MET", "mg");
        tmp.put("LYS", "mg");
        tmp.put("LEU", "mg");
        //Fats & fatty acids
        tmp.put("FAT", "g");
        tmp.put("FAMS", "g");
        tmp.put("FAPU", "g");
        tmp.put("FASAT", "g");
        tmp.put("FATRN", "g");
        tmp.put("F4D0", "mg"); //%T or g?
        tmp.put("F6D0", "mg");
        tmp.put("F8D0", "mg");
        tmp.put("F10D0", "mg");
        tmp.put("F10D1", "mg");
        tmp.put("F12D0", "mg");
        tmp.put("F13D0", "mg");
        tmp.put("F14D0", "mg");
        tmp.put("F14D1", "mg");
        tmp.put("F15D0", "mg");
        tmp.put("F15D1", "mg");
        tmp.put("F16D0", "mg");
        tmp.put("F16D1", "mg");
        tmp.put("F16D1T", "mg"); //trans
        tmp.put("F16D1T", "mg"); //cis
        tmp.put("F17D0", "mg");
        tmp.put("F17D1", "mg");
        tmp.put("F18D0", "mg");
        tmp.put("F18D1C", "mg");
        tmp.put("F18D1TN7", "mg");
        tmp.put("F18D1", "mg");
        tmp.put("F18D2", "mg");
        tmp.put("F18D2CN6", "mg");
        tmp.put("F18D2TT", "mg");
        tmp.put("F18D2I", "mg");
        tmp.put("F18D2N6", "mg");
        tmp.put("F18D3", "mg");
        tmp.put("F18D3T", "mg");
        tmp.put("F18D3I", "mg");
        tmp.put("F18D3N6", "mg");
        tmp.put("F18D3N3F", "mg"); // per unit
        tmp.put("F18D3N3", "mg"); // per %
        tmp.put("F18D4", "mg");
        tmp.put("F18D4N3", "mg");
        tmp.put("F19D0", "mg");
        tmp.put("F20D0", "mg");
        tmp.put("F20D1", "mg");
        tmp.put("F20D1N11F", "mg");
        tmp.put("F20D2N6", "mg");
        tmp.put("F20D3N3", "mg");
        tmp.put("F20D3", "mg");
        tmp.put("F20D4", "mg");
        tmp.put("F20D4N6", "mg");
        tmp.put("F20D5", "mg");
        tmp.put("F21D0", "mg");
        tmp.put("F22D0", "mg");
        tmp.put("F22D1", "mg");
        tmp.put("F22D1C", "mg");
        tmp.put("F22D1T", "mg");
        tmp.put("F22D3", "mg");
        tmp.put("F22D5N3", "mg");
        tmp.put("F22D6", "mg");
        tmp.put("F22D6N3", "mg");
        tmp.put("F23D0", "mg");
        tmp.put("F24D0", "mg");
        tmp.put("F24D1", "mg");
        tmp.put("F24D1C", "mg");
        tmp.put("LCW3TOTAL", "mg");
        tmp.put("FAUNDIFF", "mg");

        //acids
        tmp.put("ACEAC", "g");
        tmp.put("CITAC", "g");
        tmp.put("FUMAC", "g");
        tmp.put("LACAC", "g");
        tmp.put("MALAC", "g");
        tmp.put("OXALAC", "g");
        tmp.put("PROPAC", "g");
        tmp.put("QUINAC", "g");
        tmp.put("SHIKAC", "g");
        tmp.put("SUCAC", "g");
        tmp.put("TARAC", "g");


//        tmp.put("", "");
        //Minerals & metals
        tmp.put("AL", "ug");
        tmp.put("AS", "ug");
        tmp.put("CA", "mg");
        tmp.put("CD", "ug");
        tmp.put("CU", "mg");
        tmp.put("CO", "ug");
        tmp.put("CR", "ug");
        tmp.put("FD", "ug");
        tmp.put("FE", "mg");
        tmp.put("HG", "ug");
        tmp.put("ID", "ug");
        tmp.put("K", "mg");
        tmp.put("MG", "mg");
        tmp.put("MN", "mg");
        tmp.put("M0", "ug");
        tmp.put("NA", "mg");
        tmp.put("NI", "ug");
        tmp.put("P", "mg");
        tmp.put("PB", "ug");
        tmp.put("S", "mg");
        tmp.put("SE", "ug");
        tmp.put("SN", "ug");
        tmp.put("ZN", "mg");
        //Vitamins
        tmp.put("BIOT", "ug");
        tmp.put("B1", "mg");
        tmp.put("B2", "mg");
        tmp.put("B3", "mg");
        tmp.put("VITB6", "mg");
        tmp.put("VITB12", "ug");
        tmp.put("CARTA", "ug");
        tmp.put("CARTB", "ug");
        tmp.put("CARTBEQ", "ug");
        tmp.put("CHOLN", "ug");
        tmp.put("CRYPX", "ug");
        tmp.put("NIA", "mg");
        tmp.put("NIAEQ", "mg");

        tmp.put("FOL", "ug");
        tmp.put("FOLAC", "ug");
        tmp.put("FOLFD", "ug");
        tmp.put("FOLDFE", "ug");
        tmp.put("LUT+ZEA", "ug");
        tmp.put("LUTN", "ug");
        tmp.put("LYCPN", "ug");

        tmp.put("MK4", "ug");
        tmp.put("PANTAC", "mg");
        tmp.put("VITB6A", "mg");
        tmp.put("VITB12", "ug");
        tmp.put("VITE", "mg");
        tmp.put("VITK", "ug");
        tmp.put("VITK1D", "ug");
        tmp.put("RETOL", "ug");
        tmp.put("VITA_IU", "");
        tmp.put("VITA", "ug");
        tmp.put("VITC", "mg");
        //vit d
        tmp.put("CHOCAL", "ug");
        tmp.put("ERGCAL", "ug");
        tmp.put("CHOCALOH", "ug");
        tmp.put("ERGCALOH", "ug");
        tmp.put("VITDEQ", "ug");
        tmp.put("VITD", "ug");
        //sterols
        tmp.put("PHYSTR", "mg");
        tmp.put("STID7", "mg"); //usually delta 7-stigmasterol
        tmp.put("CAMD5", "mg");
        tmp.put("SITSTR", "mg");
        tmp.put("TOCPHA", "mg");
        tmp.put("TOCTRA", "mg");
        tmp.put("TOCPHB", "mg");
        tmp.put("TOCTRB", "mg");
        tmp.put("TOCPHD", "mg");
        tmp.put("TOCTRD", "mg");
        tmp.put("TOCPHG", "mg");
        tmp.put("TOCTRG", "mg");
        //Other
        tmp.put("CAFFN", "mg");
        tmp.put("CHOLE", "mg");
        defaultUnitsMapping = Collections.unmodifiableMap(tmp);

    }


    public static String getName(String abbreviation) {
        return nameMapping.get(abbreviation);
    }
    public static String getDefaultUnits(String abbreviation) {
        return defaultUnitsMapping.get(abbreviation);
    }
}
