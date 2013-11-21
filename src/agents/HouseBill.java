package agents;

public class HouseBill {
        private float balance;
        private HousePerson owner;
        private HousePerson renter;
        
        public HouseBill(float b, HousePerson r, HousePerson o)
        {
                balance = b;
                owner = o;
                renter = r;
        }
        
        public float getBalance()
        {
                return balance;
        }
        
        public HousePerson getOwner()
        {
                return owner;
        }
        
        public HousePerson getRenter()
        {
                return renter;
        }
}