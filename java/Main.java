import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Products {
    int productA = 20;
    int productB = 40;
    int productC = 50;

    int giftWrap = 1;
    int inOnePack = 10;
    int onePackCost = 5;
}

class Cart {
    int productA = 0;
    int productB = 0;
    int productC = 0;

    Cart(int productA, int productB, int productC) {
        this.productA = productA;
        this.productB = productB;
        this.productC = productC;
    }

    int totalCartAmount = 0;
}

public class Main {

    public static void main(String[] args) {
        // Creating a cart object
        Products p1 = new Products();
        Cart c1 = new Cart(0, 0, 0);

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the quantity of ProductA: ");
        c1.productA = sc.nextInt();
        System.out.print("Enter the quantity of ProductB: ");
        c1.productB = sc.nextInt();
        System.out.print("Enter the quantity of ProductC: ");
        c1.productC = sc.nextInt();

        c1.totalCartAmount = cartSum(c1, p1);
        Map<String, Double> discounts = discountRules(c1, p1);

        // Find and display the key-value pair with the highest discount
        Map.Entry<String, Double> maxDiscountEntry = findMaxDiscount(discounts);
        if (maxDiscountEntry != null) {
            displayReceipt(c1, discounts, maxDiscountEntry);
        } else {
            System.out.println("No discounts applied.");
        }
    }

    public static int cartSum(Cart obj, Products p1) {
        int total = 0;
        total = obj.productA * p1.productA + obj.productB * p1.productB + obj.productC * p1.productC;
        return total;
    }

    public static Map<String, Double> discountRules(Cart c1, Products p1) {
        Map<String, Double> map = new HashMap<>();
        int totalQuantity = c1.productA + c1.productB + c1.productC;
        if (c1.totalCartAmount > 200) {
            map.put("Flat_10_discount", 10.0);
        }
        if (c1.productA > 10 || c1.productB > 10 || c1.productC > 10) {
            double temp = c1.totalCartAmount * 0.05;
            map.put("Bulk_5_discount", temp);
        }
        if (totalQuantity > 20) {
            double temp = c1.totalCartAmount * 0.1;
            map.put("Bulk_10_discount", temp);
        }
        if (totalQuantity > 30 && (c1.productA > 15 || c1.productB > 15 || c1.productC > 15)) {
            double tempTotalCartAmount = 0;

            for (String cartItem : new String[]{"productA", "productB", "productC"}) {
                int quantity = 0;
                int price = 0;

                switch (cartItem) {
                    case "productA":
                        quantity = c1.productA;
                        price = p1.productA;
                        break;
                    case "productB":
                        quantity = c1.productB;
                        price = p1.productB;
                        break;
                    case "productC":
                        quantity = c1.productC;
                        price = p1.productC;
                        break;
                }

                if (quantity > 15) {
                    double temp1 = price * (quantity - 15) * 0.5;
                    double temp2 = price * (quantity - 15);
                    tempTotalCartAmount += price * 15 + (temp2 - temp1);
                } else {
                    tempTotalCartAmount += price * quantity;
                }
            }

            if (totalQuantity > 30) {
                map.put("Tiered_50_discount", c1.totalCartAmount - tempTotalCartAmount);
            }
        }

        return map;
    }

    public static void displayReceipt(Cart cart, Map<String, Double> discounts, Map.Entry<String, Double> maxDiscountEntry) {
        System.out.println("Product Details:");
        System.out.printf("%-10s %-10s %-10s\n", "Product", "Quantity", "Total");
        for (Map.Entry<String, Integer> entry : Map.of("productA", cart.productA, "productB", cart.productB, "productC", cart.productC).entrySet()) {
            System.out.printf("%-10s %-10s $%-10s\n", entry.getKey(), entry.getValue(), entry.getValue() * getProductPrice(entry.getKey()));
        }

        System.out.println("\nSubtotal: $" + cart.totalCartAmount);
        System.out.println("Discount Applied (" + maxDiscountEntry.getKey() + "): $" + maxDiscountEntry.getValue());
        System.out.println("Shipping Fee: $" + calculateShippingFee(cart));
        System.out.println("Gift Wrap Fee: $" + calculateGiftWrapFee(cart));

        double total = cart.totalCartAmount - maxDiscountEntry.getValue() + calculateShippingFee(cart) + calculateGiftWrapFee(cart);
        System.out.println("\nTotal: $" + total);
    }

    public static Map.Entry<String, Double> findMaxDiscount(Map<String, Double> discounts) {
        Map.Entry<String, Double> maxEntry = null;

        for (Map.Entry<String, Double> entry : discounts.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }

        return maxEntry;
    }

    public static double getProductPrice(String productName) {
        switch (productName) {
            case "productA":
                return new Products().productA;
            case "productB":
                return new Products().productB;
            case "productC":
                return new Products().productC;
            default:
                return 0;
        }
    }

    public static double calculateShippingFee(Cart cart) {
        int totalUnits = cart.productA + cart.productB + cart.productC;
        int totalPackages = (int) Math.ceil((double) totalUnits / new Products().inOnePack);
        return totalPackages * new Products().onePackCost;
    }

    public static double calculateGiftWrapFee(Cart cart) {
        return cart.productA * new Products().giftWrap + cart.productB * new Products().giftWrap + cart.productC * new Products().giftWrap;
    }
}
