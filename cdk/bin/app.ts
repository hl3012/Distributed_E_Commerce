import * as cdk from 'aws-cdk-lib';
import { EcrStack } from '../lib/ecr_stack';
import { EcsStack } from '../lib/ecs_stack';
import { PipelineStack } from '../lib/pipeline_stack';

const app = new cdk.App();

const env = {
    account: process.env.CDK_DEFAULT_ACCOUNT,
    region: process.env.CDK_DEFAULT_REGION || 'us-east-1'
};

const appName = 'onlineshopping';
const stage = 'prod';


const ecrStack = new EcrStack(app, `${appName}-ecr`, {
    env,
    repositoryName: appName
});


const ecsStack = new EcsStack(app, `${appName}-ecs-${stage}`, {
    env,
    appName,
    stage,
    repository: ecrStack.repository
});


const pipelineStack = new PipelineStack(app, `${appName}-pipeline-${stage}`, {
    env,
    appName,
    stage,
    ecrRepository: ecrStack.repository,
    ecsService: ecsStack.service,
    ecsCluster: ecsStack.cluster
});

ecsStack.addDependency(ecrStack);
pipelineStack.addDependency(ecsStack);

app.synth();