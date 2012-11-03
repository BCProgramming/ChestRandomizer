package com.BASeCamp.SurvivalChests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

import net.minecraft.server.Path;
//Special class for generating Item names.

public class NameGenerator {

	public static String[] Verbs = new String[]
	                                         
 {"accepting","addition","admiration","admission","advising","affording","alerting","allowing",
		"amusing","analysing","announcing","annoying","answering","apologising","appearing","applauding",
		"appreciating","approving","arguing","arranging","arresting","arriving","asking","attaching",
		"attacking","attempting","attending","attracting","avoiding","backing","baking","balancing",
		"baning","banging","baring","bating","bathing","battling","beaming","begging","behaving",
		"belonging","bleaching","blessing","blinding","blinking","blotting","blushing","boasting",
		"boiling","bolting","bombing","booking","boring","borrowing","bouncing","boxing","braking",
		"branching","breathing","bruising","brushing","bubbling","bumping","burning","burying","buzzing",
		"calculating","calling","camping","caring","carrying","carving","causing","challenging",
		"changing","charging","chasing","cheating","checking","cheering","chewing","choking",
		"chopping","mastication","claiming","claping","cleaning","clearing","clipping",
		"closing","coaching","coiling","collecting","colouring","combing","commanding",
		"communicating","comparing","competing","complaining","completing","concentrating",
		"concerning","confession","confusion","connecting","considering","consisting","containing",
		"continuing","copying","correcting","coughing","counting","covering","cracking","crashing",
		"crawling","crossing","crushing","crying","curing","curling","curving","cycling","damning",
		"damaging","dancing","daring","decaying","deceiving","deciding","decorating","delaying",
		"delighting","delivering","depending","describing","deserting","deserving","destroying",
		"detecting","developing","disagreement","disappearing","disapproving","disarming",
		"discovering","disliking","dividing","doubling","doubting","dragging","draining",
		"dreaming","dressing","dripping","dropping","drowning","druming","drying","dusting",
		"earning","educating","embarrassing","employing","emptying","encouraging","ending",
		"enjoying","entering","entertaining","escaping","examining","exciting","excusing",
		"exercising","existing","expanding","expecting","explaining","exploding","extending",
		"facing","fading","failing","fancying","fastening","faxing","fearing","fencing",
		"fetching","filing","filling","filming","firing","fitting","fixing","flapping",
		"flashing","floating","flooding","flowing","flowering","folding","following",
		"fooling","forcing","forming","founding","framing","frightening","frying",
		"gathering","gazing","glowing","gluing","grabing","grating","greasing",
		"greeting","grining","griping","groaning","guaranteeing","guarding",
		"guessing","guiding","hammering","handing","handling","hanging","happening","harassing",
		"harming","hating","haunting","beheading","healing","heaping","heating","helping","hooking",
		"hoping","hovering","hugging","humming","hunting","hurrying","identifying","ignoring","imagining",
		"impressing","improving","including","increasing","influencing","informing","injecting","injuring",
		"instructing","intending","interesting","interfering","interrupting","introducing","inventing",
		"inviting","irritating","itching","jailing","jaming","joging","joining","joking","judging","juggling",
		"jumping","kicking","killing","kissing","kneeling","knitting","knocking","knotting","labeling",
		"landing","lasting","laughing","launching","learning","leveling","licensing","licking","lightening",
		"liking","listing","listening","living","loading","locking","longing","looking","loving","managing",
		"marching","marking","marrying","matching","mating","mattering","measuring","meddling","melting",
		"memorising","mending","mess uping","milking","mining","missing","mixing","moaning","mooring",
		"mourning","moving","muddling","muging","multiplying","murdering","nailing","naming","needing",
		"nesting","noding","noting","noticing","numbering","obeying","objecting","observing","obtaining",
		"occuring","offending","offering","opening","ordering","overflowing","owing","owning","packing",
		"paddling","painting","parking","parting","passing","pasting","pating","pausing","pecking","pedaling",
		"peeling","peeping","performing","permiting","phoning","picking","pinching","pining","placing",
		"planing","planting","playing","pleasing","pluging","pointing","poking","polishing","poping",
		"possessing","posting","pouring","practising","praying","preaching","preceding","prefering",
		"preparing","presenting","preserving","pressing","pretending","preventing","pricking","printing",
		"producing","programing","promising","protecting","providing","pulling","pumping","punching","puncturing",
		"punishing","pushing","questioning","queuing","racing","radiating","raining","raising","reaching","realising",
		"receiving","recognising","recording","reducing","reflecting","refusing","regreting","reigning","rejecting","rejoicing","relaxing","releasing","relying","remaining","remembering","reminding","removing","repairing","repeating","replacing","replying","reporting","reproducing","requesting","rescuing","retiring","returning","rhyming","rinsing","risking","robing","rocking","rolling","roting","rubing","ruining","ruling","rushing","sacking","sailing","satisfying","saving","sawing","scaring","scattering","scolding","scorching","scrapping","scratching","screaming","screwing","scribbling","scrubing","sealing","searching","separating","serving","settling","shading","sharing","shaving","sheltering","shivering","shocking","shopping","shruging","sighing","signing","signaling","sining","sipping","skiing","skipping","slapping","slipping","slowing","smashing","smelling","smiling","smoking","snatching","sneezing","sniffing","snoring","snowing","soaking","soothing","sounding","sparing","sparking","sparkling","spelling","spilling","spoiling","spoting","spraying","sprouting","squashing","squeaking","squealing","squeezing","staining","stampping","staring","starting","staying","steering","stepping","stiring","stitching","stopping","storing","strapping","strengthening","stretching","stripping","stroking","stuffing","subtracting","succeeding","sucking","suffering","suggesting","suiting","supplying","supporting","supposing","surprising","surrounding","suspecting","suspending","switching","talking","taming","tapping","tasting","teasing","telephoning","tempting","terrifying","testing","thanking","thawing","ticking","tickling","tiing","timing","tipping","tiring","touching","touring","towing","tracing","trading","training","transporting","trapping","traveling","treating","trembling","tricking","tripping","troting","troubling","trusting","trying","tuging","tumbling","turning","twisting","typing","undressing","unfastening","uniting","unlocking","unpacking","untidying","using","vanishing","visiting","wailing","waiting","walking","wandering","wanting","warming","warning","washing","wasting","watching","watering","waving","weighing","welcoming","whining","whipping","whirling","whispering","whistling","winking","wiping","wishing","wobbling","wondering","working","worrying","wrapping","wrecking","wrestling","wriggling","x-raying","yawning","yelling","zipping","zooming"};
	
	static String[] Adjectives = new String[]
	                                                {"aback","abaft","abandoned","abashed","aberrant","abhorrent","abiding","abject","ablaze","able","abnormal","aboard","aboriginal","abortive","abounding","abrasive","abrupt","absent","absorbed","absorbing","abstracted","absurd","abundant","abusive","acceptable","accessible","accidental","accurate","acid","acidic","acoustic","acrid","actually","ad hoc","adamant","adaptable","addicted","adhesive","adjoining","adorable","adventurous","afraid","aggressive","agonizing","agreeable","ahead","ajar","alcoholic","alert","alike","alive","alleged","alluring","aloof","amazing","ambiguous","ambitious","amuck","amused","amusing","ancient","angry","animated","annoyed","annoying","anxious","apathetic","aquatic","aromatic","arrogant","ashamed","aspiring","assorted","astonishing","attractive","auspicious","automatic","available","average","awake","aware","awesome","awful","axiomatic","bad","barbarous","bashful","bawdy","beautiful","befitting","belligerent","beneficial","bent","berserk","best","better","bewildered","big","billowy","bite-sized","bitter","bizarre","black","black-and-white","bloody","blue","blue-eyed","blushing","boiling","boorish","bored","boring","bouncy","boundless","brainy","brash","brave","brawny","breakable","breezy","brief","bright","bright","broad","broken","brown","bumpy","burly","bustling","busy","cagey","calculating","callous","calm","capable","capricious","careful","careless","caring","cautious","ceaseless","certain","changeable","charming","cheap","cheerful","chemical","chief","childlike","chilly","chivalrous","chubby","chunky","clammy","classy","clean","clear","clever","cloistered","cloudy","closed","clumsy","cluttered","coherent","cold","colorful","colossal","combative","comfortable","common","complete","complex","concerned","condemned","confused","conscious","cooing","cool","cooperative","coordinated","courageous","cowardly","crabby","craven","crazy","creepy","crooked","crowded","cruel","cuddly","cultured","cumbersome","curious","curly","curved","curvy","cut","cute","cute","cynical","daffy","daily","damaged","damaging","damp","dangerous","dapper","dark","dashing","dazzling","dead","deadpan","deafening","dear","debonair","decisive","decorous","deep","deeply","defeated","defective","defiant","delicate","delicious","delightful","demonic","delirious","dependent","depressed","deranged","descriptive","deserted","detailed","determined","devilish","didactic","different","difficult","diligent","direful","dirty","disagreeable","disastrous","discreet","disgusted","disgusting","disillusioned","dispensable","distinct","disturbed","divergent","dizzy","domineering","doubtful","drab","draconian","dramatic","dreary","drunk","dry","dull","dusty","dusty","dynamic","dysfunctional","eager","early","earsplitting","earthy","easy","eatable","economic","educated","efficacious","efficient","eight","elastic","elated","elderly","electric","elegant","elfin","elite","embarrassed","eminent","empty","enchanted","enchanting","encouraging","endurable","energetic","enormous","entertaining","enthusiastic","envious","equable","equal","erect","erratic","ethereal","evanescent","evasive","even","excellent","excited","exciting","exclusive","exotic","expensive","extra-large","extra-small","exuberant","exultant","fabulous","faded","faint","fair","faithful","fallacious","false","familiar","famous","fanatical","fancy","fantastic","far","far-flung","fascinated","fast","fat","faulty","fearful","fearless","feeble","feigned","female","fertile","festive","few","fierce","filthy","fine","finicky","first","five","fixed","flagrant","flaky","flashy","flat","flawless","flimsy","flippant","flowery","fluffy","fluttering","foamy","foolish","foregoing","forgetful","fortunate","four","frail","fragile","frantic","free","freezing","frequent","fresh","fretful","friendly","frightened","frightening","full","fumbling","functional","funny","furry","furtive","future","futuristic","fuzzy","gabby","gainful","gamy","gaping","garrulous","gaudy","general","gentle","giant","giddy","gifted","gigantic","glamorous","gleaming","glib","glistening","glorious","glossy","godly","good","goofy","gorgeous","graceful","grandiose","grateful","gratis","gray","greasy","great","greedy","green","grey","grieving","groovy","grotesque","grouchy","grubby","gruesome","grumpy","guarded","guiltless","gullible","gusty","guttural","habitual","half","hallowed","halting","handsome","handsomely","handy","hanging","hapless","happy","hard","hard-to-find","harmonious","harsh","hateful","heady","healthy","heartbreaking","heavenly","heavy","hellish","helpful","helpless","hesitant","hideous","high","highfalutin","high-pitched","hilarious","hissing","historical","holistic","hollow","homeless","homely","honorable","horrible","hospitable","hot","huge","hulking","humdrum","humorous","hungry","hurried","hurt","hushed","husky","hypnotic","hysterical","icky","icy","idiotic","ignorant","ill","illegal","ill-fated","ill-informed","illustrious","imaginary","immense","imminent","impartial","imperfect","impolite","important","imported","impossible","incandescent","incompetent","inconclusive","industrious","incredible","inexpensive","infamous","innate","innocent","inquisitive","insidious","instinctive","intelligent","interesting","internal","invincible","irate","irritating","itchy","jaded","jagged","jazzy","jealous","jittery","jobless","jolly","joyous","judicious","juicy","jumbled","jumpy","juvenile","kaput","keen","kind","kindhearted","kindly","knotty","knowing","knowledgeable","known","labored","lackadaisical","lacking","lame","lamentable","languid","large","last","late","laughable","lavish","lazy","lean","learned","left","legal","lethal","level","lewd","light","like","likeable","limping","literate","little","lively","lively","living","lonely","long","longing","long-term","loose","lopsided","loud","loutish","lovely","loving","low","lowly","lucky","ludicrous","lumpy","lush","luxuriant","lying","lyrical","macabre","macho","maddening","madly","magenta","magical","magnificent","majestic","makeshift","male","malicious","mammoth","maniacal","many","marked","massive","married","marvelous","material","materialistic","mature","mean","measly","meaty","medical","meek","mellow","melodic","melted","merciful","mere","messy","mighty","military","milky","mindless","miniature","minor","miscreant","misty","mixed","moaning","modern","moldy","momentous","motionless","mountainous","muddled","mundane","murky","mushy","mute","mysterious","naive","nappy","narrow","nasty","natural","naughty","nauseating","near","neat","nebulous","necessary","needless","needy","neighborly","nervous","new","next","nice","nifty","nimble","nine","nippy","noiseless","noisy","nonchalant","nondescript","nonstop","normal","nostalgic","nosy","noxious","null","numberless","numerous","nutritious","nutty","oafish","obedient","obeisant","obese","obnoxious","obscene","obsequious","observant","obsolete","obtainable","oceanic","odd","offbeat","old","old-fashioned","omniscient","one","onerous","open","opposite","optimal","orange","ordinary","organic","ossified","outgoing","outrageous","outstanding","oval","overconfident","overjoyed","overrated","overt","overwrought","painful","painstaking","pale","paltry","panicky","panoramic","parallel","parched","parsimonious","past","pastoral","pathetic","peaceful","penitent","perfect","periodic","permissible","perpetual","petite","petite","phobic","physical","picayune","pink","piquant","placid","plain","plant","plastic","plausible","pleasant","plucky","pointless","poised","polite","political","poor","possessive","possible","powerful","precious","premium","present","pretty","previous","pricey","prickly","private","probable","productive","profuse","protective","proud","psychedelic","psychotic","public","puffy","pumped","puny","purple","purring","pushy","puzzled","puzzling","quack","quaint","quarrelsome","questionable","quick","quickest","quiet","quirky","quixotic","quizzical","rabid","racial","ragged","rainy","rambunctious","rampant","rapid","rare","raspy","ratty","ready","real","rebel","receptive","recondite","red","redundant","reflective","regular","relieved","remarkable","reminiscent","repulsive","resolute","resonant","responsible","rhetorical","rich","right","righteous","rightful","rigid","ripe","ritzy","roasted","robust","romantic","roomy","rotten","rough","round","royal","ruddy","rude","rural","rustic","ruthless","sable","sad","safe","salty","same","sassy","satisfying","savory","scandalous","scarce","scared","scary","scattered","scientific","scintillating","scrawny","screeching","second","second-hand","secret","secretive","sedate","seemly","selective","selfish","separate","serious","shaggy","shaky","shallow","sharp","shiny","shivering","shocking","short","shrill","shut","shy","sick","silent","silent","silky","silly","simple","simplistic","sincere","six","skillful","skinny","sleepy","slim","slimy","slippery","sloppy","slow","small","smart","smelly","smiling","smoggy","smooth","sneaky","snobbish","snotty","soft","soggy","solid","somber","sophisticated","sordid","sore","sore","sour","sparkling","special","spectacular","spicy","spiffy","spiky","spiritual","spiteful","splendid","spooky","spotless","spotted","spotty","spurious","squalid","square","squealing","squeamish","staking","stale","standing","statuesque","steadfast","steady","steep","stereotyped","sticky","stiff","stimulating","stingy","stormy","straight","strange","striped","strong","stupendous","stupid","sturdy","subdued","subsequent","substantial","successful","succinct","sudden","sulky","super","superb","superficial","supreme","swanky","sweet","sweltering","swift","symptomatic","synonymous","taboo","tacit","tacky","talented","tall","tame","tan","tangible","tangy","tart","tasteful","tasteless","tasty","tawdry","tearful","tedious","teeny","teeny-tiny","telling","temporary","ten","tender","tense","tense","tenuous","terrible","terrific","tested","testy","thankful","therapeutic","thick","thin","thinkable","third","thirsty","thirsty","thoughtful","thoughtless","threatening","three","thundering","tidy","tight","tightfisted","tiny","tired","tiresome","toothsome","torpid","tough","towering","tranquil","trashy","tremendous","tricky","trite","troubled","truculent","true","truthful","two","typical","ubiquitous","ugliest","ugly","ultra","unable","unaccountable","unadvised","unarmed","unbecoming","unbiased","uncovered","understood","undesirable","unequal","unequaled","uneven","unhealthy","uninterested","unique","unkempt","unknown","unnatural","unruly","unsightly","unsuitable","untidy","unused","unusual","unwieldy","unwritten","upbeat","uppity","upset","uptight","used","useful","useless","utopian","utter","uttermost","vacuous","vagabond","vague","valuable","various","vast","vengeful","venomous","verdant","versed","victorious","vigorous","violent","violet","vivacious","voiceless","volatile","voracious","vulgar","wacky","waggish","waiting","wakeful","wandering","wanting","warlike","warm","wary","wasteful","watery","weak","wealthy","weary","well-groomed","well-made","well-off","well-to-do","wet","whimsical","whispering","white","whole","wholesale","wicked","wide","wide-eyed","wiggly","wild","willing","windy","wiry","wise","wistful","witty","woebegone","womanly","wonderful","wooden","woozy","workable","worried","worthless","wrathful","wretched","wrong","wry","yellow","yielding","young","youthful"};
	
	static String[] PrefixValues = new String[] 
	     {"Hot","Cold","Blazing","Burning","Freezing","Extreme","oversized","rotund","particular","mean","zistonian",
		"Canadian","american","british","Hispanic","jewish","Irish","Scottish","Incredible","Ordinary"};
		
	
	
	//adds verbs from an existing file. return values less than 0 indicate failure.

	public static String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}
	
	public static String GenerateName(String[] BaseNames,String[] Adjectives,String[] Verbs){
	
		
		StringBuffer sb = new StringBuffer();
		//first array is base name; eg, "Sword, Blade, Slicer, Katana, etc.
		//second array is adjectives; "imminent","bloody","extreme","metallic","superlative","powerful","thirstful","vengeful","smelly","stinky","immaculate","particular"
		//third array is verbs; bloodening,cutting,slicing,revenge
		
		
		String prefix = "";
		if(RandomData.rgen.nextFloat() > 0.75){
			prefix = RandomData.Choose(PrefixValues);
			
		}
		if(prefix.length()>0) prefix = prefix + " ";
		String basename = RandomData.Choose(BaseNames);
		
		
		String adjective = RandomData.rgen.nextFloat()>0.5?RandomData.Choose(Adjectives):"";
		String Verb = RandomData.Choose(Verbs);
		
		sb.append(prefix);
		sb.append(basename);
		if(adjective!=""){
			sb.append(" of " + adjective + " " + Verb);
			
		}
		
		return toTitleCase(sb.toString());
		
		
	}
	
	
}
