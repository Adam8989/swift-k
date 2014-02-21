/*
 * job-description.h
 *
 *  Created on: Jun 9, 2012
 *      Author: mike
 */

#ifndef JOB_H_
#define JOB_H_

#include "StagingSetEntry.h"
#include "JobStatus.h"
#include <string>
#include <vector>
#include <map>

using namespace std;

class Job {
	private:
		string identity;
		string executable;
		vector<string*>* arguments;
		string* directory;
		string* stdinLocation;
		string* stdoutLocation;
		string* stderrLocation;
        string jobManager;

		map<string, string>* env;
		map<string, string>* attributes;
		vector<StagingSetEntry>* stageIns;
		vector<StagingSetEntry>* stageOuts;
		vector<string>* cleanups;

		string* stdout;
		string* stderr;
		JobStatus* status;
	public:
		Job(string executable);
		virtual ~Job();

		string& getExecutable();

		string& getIdentity();

		vector<string*>* getArguments();
		void addArgument(string& arg);
		void addArgument(const char* arg);

		string* getDirectory();
		void setDirectory(string& directory);

		string* getStdinLocation();
		void setStdinLocation(string& loc);

		string* getStdoutLocation();
		void setStdoutLocation(string& loc);

		string* getStderrLocation();
		void setStderrLocation(string& loc);

        string getJobManager();
        void setJobManager(string jm);

		map<string, string>* getEnv();
		string* getEnv(string name);
		void setEnv(string name, string value);

		map<string, string>* getAttributes();
		string* getAttribute(string name);
		void setAttribute(string name, string value);

		vector<StagingSetEntry>* getStageIns();
		void addStageIn(string src, string dest, StagingMode mode);

		vector<StagingSetEntry>* getStageOuts();
		void addStageOut(string src, string dest, StagingMode mode);

		vector<string>* getCleanups();
		void addCleanup(string cleanup);

		string* getStderr();
		string* getStdout();

		JobStatus* getStatus();
		void setStatus(JobStatus* status);
};

#endif /* JOB_DESCRIPTION_H_ */
