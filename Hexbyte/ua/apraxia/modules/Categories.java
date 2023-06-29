package ua.apraxia.modules;

public enum Categories {
     Combat {
        @Override
        public String getChar() {
            return "a";
        }
     }, Legit {
        @Override
        public String getChar() {
            return "k";
        }
    }, Movement {
        @Override
        public String getChar() {
            return "d";
        }
    }, Player {
        @Override
        public String getChar() {
            return "h";
        }
    }, Exploit {
        @Override
        public String getChar() {
            return "b";
        }
    }, Render {
        @Override
        public String getChar()
        {
            return "e";
        }
    }, Display {
        @Override
        public String getChar()
        {
            return "f";
        }
    }, World {
        @Override
        public String getChar() {
            return "r";
        }
    }, Other {
        @Override
        public String getChar() {
            return "w";
        }

    };

    public abstract String getChar();
}
