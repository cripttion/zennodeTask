//Input taking interface
const readline = require('readline');
const readLine = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
});


//project object
const products = {
    productA: 20,
    productB: 40,
    productC: 50,
};

const giftWrap = 1;
const inOnePack = 10;
const onePackCost = 5;
//function for discount rule 

const discountRules ={
    flat_10_discount:(cart,totalCartAmout)=>{
       return totalCartAmout>200?10:0;
    },
    bulk_5_discount:(cart,totalCartAmout)=>{
        let temp = false;
        Object.entries(cart).map(entry=>{
            if(entry[1]>10)
            {
                temp = true;
            }
        });
      return temp?totalCartAmout*0.05:0;
    },
    bulk_10_discount:(cart,totalCartAmout)=>{
        let totalQuantity = 0;
        Object.entries(cart).map(entry=>{
            totalQuantity = totalQuantity+entry[1];
        })
        console.log(totalQuantity);
        return totalQuantity>20 ? totalCartAmout*0.1:0;
    },
    tiered_50_discount:(cart,totalCartAmout)=>{
        let totalQuantity = 0;
  let tempCart = {};
  let anyTemp = false;

  Object.entries(cart).map((entry) => {
    if (entry[1] > 15) {
      anyTemp = true;
    }
    totalQuantity = totalQuantity + entry[1];
  });
  let tempTotalCartAmount = 0;

  for (const cartItem in cart) {
    const quantity = cart[cartItem];
    const price = products[cartItem];
  
    // Check if the quantity is greater than 15
    if (!isNaN(quantity) && !isNaN(price)) {
      if (quantity > 15) {
        const temp1 = (price * (quantity - 15))*0.5;
        const temp2 = (price * (quantity - 15));
        // Calculate the total with the discount for quantities over 15
        tempTotalCartAmount += price * 15 + (temp2-temp1);
      } else {
        // Calculate the total without the discount for quantities less than or equal to 15
        tempTotalCartAmount += price * quantity;
      }
    } else {
      console.log(`Invalid quantity or price for ${cartItem}`);
    }
  }
  
  // console.log(tempTotalCartAmount);
  return anyTemp && totalQuantity > 30 ? totalCartAmout-tempTotalCartAmount : 0;
    }
}


function cartSum(cart,products)
{
    let quantity = [];
    let value = [];

    Object.entries(cart).map(entry=>{
        quantity.push(entry[1]);
    })
    Object.entries(products).map(entry=>{
         value.push(entry[1]);
    })
  
    let total = 0;

    for(let i = 0;i<value.length;i++)
    {
        total = total+ quantity[i]*value[i];
    }
//    const total = cart.productA*products.productA + cart.productB*products.productB+cart.productC*products.productC;
    
   return total;
    
}


// Function to display the receipt
function displayReceipt(cart, subtotal, discountName, discountAmount, shippingFee, giftWrapTotal, total) {
    console.log("Product Details:");
    for (const product in cart) {
        console.log(`${product}: Quantity - ${cart[product]}, Total - $${cart[product] * products[product]}`);
    }

    console.log(`\nSubtotal: $${subtotal.toFixed(2)}`);
    console.log(`Discount Applied (${discountName}): $${discountAmount.toFixed(2)}`);
    console.log(`Shipping Fee: $${shippingFee.toFixed(2)}`);
    console.log(`Gift Wrap Fee: $${giftWrapTotal.toFixed(2)}`);

    console.log(`\nTotal: $${total.toFixed(2)}`);
}
//creating main funtion 
function main(){
   const cart={};
   let isGiftWrap;
    //taking input of product quantity;
   readLine.question("Enter the value of Product A : ",(temp)=>{
    cart.productA = parseInt(temp);

    readLine.question("Enter the value of Product B : ",(temp)=>{
        cart.productB = parseInt(temp);


      readLine.question("Enter the value of Product C : ",(temp)=>{
        cart.productC = parseInt(temp);


       readLine.question("You want gift wrap or not (yes/no): ",(temp)=>{
            //  if(temp.toLocaleLowerCase()==='yes'){
            //     cart.isGiftWrap = -
            //  }
             isGiftWrap = temp.toLowerCase()==='yes'?true:false;
             
             const totalCartAmout = cartSum(cart,products);
             let totalDiscount = 0;
             let discountName;
            //  console.log(totalCartAmout);
            
             for (const discoutrule in discountRules) {
                const discountAfterRule = discountRules[discoutrule](cart,totalCartAmout);
                // console.log(discoutrule,discountAfterRule);
                if (discountAfterRule > totalDiscount) {
                    totalDiscount = discountAfterRule;
                    discountName = discoutrule;
                }
            }
            let totalUnit=0;
            Object.entries(cart).map((entry) => {
               
                totalUnit = totalUnit + entry[1];
              });
            //   console.log(totalUnit);
              const totalPackages = Math.ceil(totalUnit /inOnePack);
              const shippingFee = totalPackages * onePackCost;
              const giftWrapTotal = isGiftWrap ? totalUnit * giftWrap : 0;

              const total = totalCartAmout - totalDiscount + shippingFee + giftWrapTotal;

           displayReceipt(cart,totalCartAmout,discountName,totalDiscount,shippingFee,giftWrapTotal,total);
         readLine.close();
       })
      })
    })
   })
}
main();
