runApplication:
	javac -cp .:src App.java Backend.java BackendInterface.java Frontend.java FrontendInterface.java GameRecord.java IterableSortedCollection.java SortedCollection.java TextUITester.java src/*.java
	java -cp .:src App

runAllTests:
	javac -cp .:src:../junit5.jar *.java src/*.java
	java -jar ../junit5.jar -cp .:src -c BackendTests

clean:
	rm -f *.class src/*.class
